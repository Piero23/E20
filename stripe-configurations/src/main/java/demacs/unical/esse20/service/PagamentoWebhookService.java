package demacs.unical.esse20.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PagamentoWebhookService {

    public void processWebhook(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> event = mapper.readValue(payload, Map.class);
            String eventType = (String) event.get("type");

            switch (eventType) {
                case "checkout.session.completed":
                case "payment_intent.succeeded":
                    log.info("Pagamento completato");
                    // qui puoi aggiornare il DB o notificare il sistema
                    break;
                case "checkout.session.expired":
                    log.info("Pagamento cancellato o scaduto");
                    // gestione cancellazione
                    break;
                default:
                    // ignora gli altri eventi
                    break;
            }
        } catch (Exception e) {
            log.error("Errore parsing webhook: {}", e.getMessage());
        }
    }
}