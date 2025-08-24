package demacs.unical.esse20.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import demacs.unical.esse20.service.PagamentoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/stripe")
@RequiredArgsConstructor
public class PagamentoController {

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

    private final PagamentoService pagamentoService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
        try {
            Session session = pagamentoService.createPaymentSession(
                    request.getNomeArticolo(),
                    request.getPrezzo(),
                    request.getValuta(),
                    request.getSuccessUrl(),
                    request.getCancelUrl()
            );

            return ResponseEntity.ok(Map.of("url", session.getUrl()));
        } catch (IllegalStateException | IllegalArgumentException | StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Errore sconosciuto: " + e.getMessage()));
        }
    }

    public static class CheckoutRequest {
        private String nomeArticolo;
        private long prezzo;
        private String valuta;
        private String successUrl;
        private String cancelUrl;

        // getters e setters
        public String getNomeArticolo() { return nomeArticolo; }
        public void setNomeArticolo(String nomeArticolo) { this.nomeArticolo = nomeArticolo; }
        public long getPrezzo() { return prezzo; }
        public void setPrezzo(long prezzo) { this.prezzo = prezzo; }
        public String getValuta() { return valuta; }
        public void setValuta(String valuta) { this.valuta = valuta; }
        public String getSuccessUrl() { return successUrl; }
        public void setSuccessUrl(String successUrl) { this.successUrl = successUrl; }
        public String getCancelUrl() { return cancelUrl; }
        public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(HttpServletRequest request) {
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore lettura payload");
        }

        String body = payload.toString();

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> event = mapper.readValue(body, Map.class);
            String eventType = (String) event.get("type");

            switch (eventType) {
                case "checkout.session.completed":
                    System.out.println("Richiesta completata");
                    break;
                case "payment_intent.succeeded":
                    System.out.println("Richiesta completata");
                    break;
                case "checkout.session.expired":
                    System.out.println("Richiesta cancellata");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Errore parsing webhook: " + e.getMessage());
        }

        return ResponseEntity.ok("success");
    }
}