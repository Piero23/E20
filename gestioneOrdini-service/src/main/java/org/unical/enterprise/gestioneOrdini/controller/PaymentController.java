package org.unical.enterprise.gestioneOrdini.controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;
import org.unical.enterprise.gestioneOrdini.dto.OrdineRequest;
import org.unical.enterprise.gestioneOrdini.service.BigliettoService;
import org.unical.enterprise.gestioneOrdini.service.MailService;
import org.unical.enterprise.gestioneOrdini.service.OrdineService;

@RestController
@RequestMapping("/api/pagamento")
@CrossOrigin(origins = "http://localhost:8084")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    OrdineService ordineService;
    BigliettoService bigliettoService;
    MailService mailService;

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;  // Setta la chiave API di Stripe
        logger.info("Chiave API di Stripe inizializzata.");
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Object> createCheckoutSession(@RequestBody OrdineRequest ordineRequest) {
        logger.info("Richiesta di creazione sessione di checkout ricevuta.");
        try {
            // Parametri della sessione di checkout
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://example.com/success")
                    .setCancelUrl("https://example.com/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice("price_1RItveP6wuNuFoXfxCFZWqIC")
                                    .build())
                    .build();

            // Creazione della sessione su Stripe
            Session session = Session.create(params);
            logger.info("Sessione di checkout creata con ID: " + session.getId());

            logger.info("Inizio Salvataggio Ordine");
            Ordine ordine=ordineService.saveOrdine(ordineRequest.ordine(), ordineRequest.biglietti());

            //ottenere mail utente acquirente
            //mailService.sendMail(mail utente acquirente, ordine);

            for(Biglietto b: bigliettoService.findAllByOrdine(ordine)){
                mailService.sendQrCodeMail(b.getEmail(), bigliettoService.getQrCode(b.getId()));
            }
            logger.info("Ordine {} Salvato", ordine.getId());

            return ResponseEntity.ok().body("{\"id\": \"" + session.getId() + "\"}");
        } catch (Exception e) {
            logger.error("Errore durante la creazione della sessione di checkout: " + e.getMessage(), e);
            e.printStackTrace();
            // Restituisci un errore in formato JSON
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Stripe checkout session creation failed\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }


    @GetMapping("/test")
    private String test() {
        return "Sono PaymentController";
    }
}