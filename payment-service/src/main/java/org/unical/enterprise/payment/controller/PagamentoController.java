package org.unical.enterprise.payment.controller;

import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.payment.service.PagamentoService;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.BigliettoDto;
import org.unical.enterprise.shared.dto.OrdineRequest;
import org.unical.enterprise.shared.dto.OrdineTransferDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class PagamentoController {
    private static final Logger logger = LoggerFactory.getLogger(PagamentoController.class);
    private final UtenteServiceClient utenteServiceClient;


    /*
    https://github.com/stripe/stripe-cli/releases/download/v1.29.0/stripe_1.29.0_windows_x86_64.zip

    & "C:\Users\andre\Desktop\Unical\Enterprise Applications\stripe.exe" listen --forward-to http://localhost:8083/stripe/webhook

    $response = Invoke-WebRequest -Uri "http://localhost:8083/stripe/checkout" `
            -Method POST `
            -ContentType "application/json" `
            -Body '{
            "nomeArticolo":"Torta Decorata",
            "prezzo":1500,
            "valuta":"eur",
            "successUrl":"https://prova1.com/success",
            "cancelUrl":"https://prova2.com/cancel",
            }'

            $data = $response.Content | ConvertFrom-Json
            Write-Output $data.url

     */

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;

    @Value("${stripe.api.webhook}")
    private String webHookSecret;

    private final PagamentoService pagamentoService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrdineTransferDto request, Authentication auth) {
        try {
            UUID user = pagamentoService.getUserIDByUsername(auth.getName());
            UUID orderUser = request.utenteId();

            if (pagamentoService.checkUtente(user)){
                if (pagamentoService.checkUtente(orderUser)){
                    if (user.equals(orderUser)) {
                        for (BigliettoDto biglietto: request.biglietti()){
//                            if(pagamentoService.findByData(biglietto.email(), biglietto.idEvento()) && pagamentoService.needsName(biglietto.idEvento()))
//                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Esiste già un biglietto per evento " + biglietto.idEvento() + " per " + biglietto.nome() + " " + biglietto.cognome());
                            if (pagamentoService.isAgeRestricted(biglietto.idEvento()) && !pagamentoService.isMaggiorenne(biglietto.dataNascita()))
                                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(biglietto.nome() + " " + biglietto.cognome() + " non ha l'età adatta per accedere all'evento.");
                            boolean cond1 = pagamentoService.needsName(biglietto.idEvento()) && (biglietto.nome()==null || biglietto.cognome()==null);
                            boolean cond2 = !pagamentoService.needsName(biglietto.idEvento()) && (biglietto.nome()!=null || biglietto.cognome()!=null);
                            if (cond1 || cond2) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La richiesta presenta dei campi errati");
                            }
                        }
                        //TODO se il numero di biglietti richiesti è più di quelli disponibili errore
                        logger.info("Iniziato Ordine da utente {}", user);
                        Session session = pagamentoService.createPaymentSession(
                                request                        );

                        return ResponseEntity.ok(Map.of("url", session.getUrl()));
                    } else {
                        logger.info("Utente {} non autorizzato", user);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente " + user + " non autorizzato");
                    }
                } else {
                    logger.error("Si sta tentando di effettuare un ordine per un utente inesistente");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Si sta tentando di effettuare un ordine per un utente inesistente");
                }
            } else {
                logger.error("Utente {} non registrato sulla piattaforma", user);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utente "+user+" non registrato sulla piattaforma");
            }

        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Errore sconosciuto: " + e.getMessage()));
        }
    }

    @GetMapping("/test")
    public String test(Authentication auth) {
        return "Ciao" + auth.getName();
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webHookSecret);
        } catch (JsonSyntaxException e) {
            return ResponseEntity.status(400).body("Invalid payload");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Invalid signature");
        }

        switch (event.getType()) {
            case "checkout.session.completed":
                pagamentoService.handlePaymentSuccess(event);
                break;
            case "payment_intent.payment_failed":
                pagamentoService.handlePaymentFailure(event);
                break;
            default:
                System.out.println("Evento non gestito: " + event.getType());
        }

        return ResponseEntity.ok("Success");
    }

}