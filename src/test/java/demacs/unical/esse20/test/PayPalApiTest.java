package demacs.unical.esse20.test;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class PayPalApiTest {

    private final String accessToken = System.getenv("access_token_andrea");
    private final String paypalPaymentUrl = "https://api.sandbox.paypal.com/v1/payments/payment";

    @Test
    public void testCreatePayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il numero della carta di credito (fittizia): ");
        String cardNumber = scanner.nextLine();
        System.out.print("Inserisci il mese di scadenza della carta: ");
        String expireMonth = scanner.nextLine();
        System.out.print("Inserisci l'anno di scadenza della carta: ");
        String expireYear = scanner.nextLine();
        System.out.print("Inserisci il CVV della carta: ");
        String cvv = scanner.nextLine();

        // Crea un'istanza RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Configura gli header con l'access token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corpo della richiesta di pagamento (usando il numero di carta dato dall'utente)
        String body = "{\n" +
                "  \"intent\": \"sale\",\n" +
                "  \"payer\": {\n" +
                "    \"payment_method\": \"credit_card\",\n" +
                "    \"funding_instruments\": [\n" +
                "      {\n" +
                "        \"credit_card\": {\n" +
                "          \"number\": \"" + cardNumber + "\",\n" +  // Numero della carta inserito dall'utente
                "          \"type\": \"visa\",\n" +
                "          \"expire_month\": \"" + expireMonth + "\",\n" +  // Mese di scadenza
                "          \"expire_year\": \"" + expireYear + "\",\n" +  // Anno di scadenza
                "          \"cvv2\": \"" + cvv + "\",\n" +  // CVV inserito dall'utente
                "          \"first_name\": \"John\",\n" +
                "          \"last_name\": \"Doe\",\n" +
                "          \"billing_address\": {\n" +
                "            \"line1\": \"123 Main St\",\n" +
                "            \"city\": \"San Jose\",\n" +
                "            \"state\": \"CA\",\n" +
                "            \"postal_code\": \"95131\",\n" +
                "            \"country_code\": \"US\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"transactions\": [\n" +
                "    {\n" +
                "      \"amount\": {\n" +
                "        \"total\": \"10.00\",\n" +
                "        \"currency\": \"USD\"\n" +
                "      },\n" +
                "      \"payee\": {\n" +
                "        \"email\": \"business_sandbox_account@paypal.com\"\n" +
                "      },\n" +
                "      \"description\": \"Test Payment\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Configura la richiesta
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        // Esegui la richiesta
        ResponseEntity<String> response = restTemplate.exchange(paypalPaymentUrl, HttpMethod.POST, requestEntity, String.class);

        // Verifica che la risposta sia stata ricevuta correttamente
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Verifica che la risposta contenga il pagamento creato
        assertTrue(response.getBody().contains("id"));
    }
}