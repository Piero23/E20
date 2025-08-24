package demacs.unical.esse20.controller;

import demacs.unical.esse20.service.PagamentoService;
import demacs.unical.esse20.service.PagamentoWebhookService;
import com.stripe.model.checkout.Session;
import com.stripe.exception.StripeException;
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

    private final PagamentoService pagamentoService;
    private final PagamentoWebhookService webhookService;

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

        webhookService.processWebhook(payload.toString());
        return ResponseEntity.ok("success");
    }
}