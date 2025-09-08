package org.unical.enterprise.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unical.enterprise.payment.OrdineServiceClient;
import org.unical.enterprise.shared.clients.EventoServiceClient;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.*;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoService {

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;

    private final EventoServiceClient eventoServiceClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OrdineServiceClient ordineServiceClient;

    private final UtenteServiceClient utenteServiceClient;

    public Session createPaymentSession(OrdineTransferDto ordineTransferDto,
                                        String successUrl, String cancelUrl) throws StripeException {

        Objects.requireNonNull(ordineTransferDto.valuta(), "valuta non può essere null");
        Objects.requireNonNull(successUrl, "successUrl non può essere null");
        Objects.requireNonNull(cancelUrl, "cancelUrl non può essere null");

        String sessionId = UUID.randomUUID().toString();

        // USA opsForValue() invece di ListOperations
        redisTemplate.opsForValue().set(
                "pending_order:" + sessionId,
                ordineTransferDto,
                Duration.ofMinutes(5)
        );

        if (stripeSecretKey == null || stripeSecretKey.isBlank()) {
            throw new IllegalStateException("La chiave Stripe non è configurata. Imposta stripe.api.secret in application.properties");
        }

        Stripe.apiKey = stripeSecretKey;

        EventoBasicDto datiEvento = eventoServiceClient.findById(ordineTransferDto.biglietti().getFirst().idEvento());

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .putMetadata("session_id", sessionId)
                .putMetadata("user_id", ordineTransferDto.utenteId().toString())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity((long) ordineTransferDto.biglietti().size())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(ordineTransferDto.valuta())
                                                .setUnitAmount((long) datiEvento.getPrezzo() * 100)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(datiEvento.getNome())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        return Session.create(params);
    }

    public Session retrieveSessionById(String sessionId) throws StripeException {
        Objects.requireNonNull(sessionId, "sessionId non può essere null");
        if (stripeSecretKey == null || stripeSecretKey.isBlank()) {
            throw new IllegalStateException("La chiave Stripe non è configurata. Imposta stripe.api.secret in application.properties");
        }
        Stripe.apiKey = stripeSecretKey;
        return Session.retrieve(sessionId);
    }

    public void processWebhook(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> event = mapper.readValue(payload, Map.class);
            String eventType = (String) event.get("type");

            switch (eventType) {
                case "checkout.session.completed":
                    log.info("Pagamento completato");
                    break;
                case "payment_intent.succeeded":
                    log.info("Pagamento completato");
                    break;
                case "checkout.session.expired":
                    log.info("Pagamento cancellato o scaduto");
                    // gestione cancellazione
                    break;
            }
        } catch (Exception e) {
            log.error("Errore parsing webhook: {}", e.getMessage());
        }
    }

    public void handlePaymentSuccess(Event event) {
        try {
            Session session = (Session) event.getData().getObject();
            String sessionId = session.getMetadata().get("session_id");

            // Recupera l'OrdineRequest usando ValueOperations
            OrdineTransferDto ordineTransferDto = (OrdineTransferDto) redisTemplate
                    .opsForValue().get("pending_order:" + sessionId);

            if (ordineTransferDto == null) {
                throw new RuntimeException("Dati ordine non trovati per session: " + sessionId);
            }

            //System.out.println(sessionId);

            EventoBasicDto datiEvento = eventoServiceClient.findById(ordineTransferDto.biglietti().getFirst().idEvento());

            ordineServiceClient.save(
                    OrdineRequest.builder()
                            .ordine(
                                    OrdineDto.builder()
                                            .importo(datiEvento.getPrezzo()*ordineTransferDto.biglietti().size())
                                            .utenteId(ordineTransferDto.utenteId())
                                            .build()
                            )
                            .biglietti(ordineTransferDto.biglietti())
                            .build()
            );

            // Rimuovi i dati dalla cache
            redisTemplate.delete("pending_order:" + sessionId);

            //System.out.println("Ordine confermato: " + ordineTransferDto.utenteId().toString());

        } catch (Exception e) {
            System.err.println("Errore nella conferma ordine: " + e.getMessage());
            // Qui potresti implementare una retry logic o logging più avanzato
        }
    }

    public void handlePaymentFailure(Event event) {
        try {
            Session session = (Session) event.getData().getObject();
            String sessionId = session.getMetadata().get("session_id");

            // Recupera l'ordine per fare logging
            OrdineRequest ordineRequest = (OrdineRequest) redisTemplate
                    .opsForValue().get("pending_order:" + sessionId);

            if (ordineRequest != null) {
                // Rimuovi i dati dalla cache visto che il pagamento è fallito
                redisTemplate.delete("pending_order:" + sessionId);
                System.out.println("Pagamento fallito per ordine utente: " + ordineRequest.ordine().utenteId());
            }

        } catch (Exception e) {
            System.err.println("Errore nella gestione del fallimento pagamento: " + e.getMessage());
        }
    }

    @Transactional
    public boolean checkUtente(UUID utente){
        try{
            utenteServiceClient.getById(utente);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Transactional
    public UUID getUserIDByUsername(String username) {
        return utenteServiceClient.getUtenteByUsername(username).getBody().getId();
    }

    public boolean findByData(String email, Long evento) {
        return ordineServiceClient.findByData(TicketCheck.builder()
                .eventID(evento)
                .eMail(email)
                .build());
    }

    public boolean isAgeRestricted(Long idEvento){
        return eventoServiceClient.findById(idEvento).isAge_restricted();
    }
    public boolean needsName(Long idEvento){
        return eventoServiceClient.findById(idEvento).isB_nominativo();
    }


}