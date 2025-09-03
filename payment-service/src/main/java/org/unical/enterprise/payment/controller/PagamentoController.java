package org.unical.enterprise.payment.controller;

import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.payment.service.PagamentoService;
import org.unical.enterprise.shared.clients.UtenteServiceClient;
import org.unical.enterprise.shared.dto.CheckoutRequest;
import org.unical.enterprise.shared.dto.OrdineRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/stripe")
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
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request, Authentication auth) {
        try {
            String user = auth.getName();

            // TODO: sistema questa cosa
            // String userUUID = utenteServiceClient.getByUsername(user).id().toString();
            UUID orderUser = request.getOrdine().utenteId();

            if (pagamentoService.checkUtente(UUID.fromString(user))){
                if (pagamentoService.checkUtente(orderUser)){
                    if (user.equals(orderUser.toString())) {
                        logger.info("Iniziato Ordine da utente {}", user);
                        Session session = pagamentoService.createPaymentSession(
                                request.getOrdine(),
                                request.getSuccessUrl(),
                                request.getCancelUrl()
                        );

                        return ResponseEntity.ok(Map.of("url", session.getUrl()));
                    } else {
                        logger.info("Utente {} non autorizzato", user);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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