package demacs.unical.esse20;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8084")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;  // Setta la chiave API di Stripe
        logger.info("Chiave API di Stripe inizializzata.");
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Object> createCheckoutSession() {
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

            return ResponseEntity.ok().body("{\"id\": \"" + session.getId() + "\"}");
        } catch (Exception e) {
            logger.error("Errore durante la creazione della sessione di checkout: " + e.getMessage(), e);
            e.printStackTrace();
            // Restituisci un errore in formato JSON
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Stripe checkout session creation failed\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // Questo ci servirà quando faremo la parte della creazione degli eventi
    @PostMapping("/create-product-checkout")
    public ResponseEntity<Map<String, String>> createProductAndCheckout() {
        try {
            // Qua creo il prodotto
            ProductCreateParams productParams = ProductCreateParams.builder()
                    .setName("Biglietto concerto di Baby Gang (non compratelo)")
                    .setDescription("Non compratelo che fa schifo")
                    .build();

            Product product = Product.create(productParams);

            // Qua ci metto il prezzo
            PriceCreateParams priceParams = PriceCreateParams.builder()
                    .setUnitAmount(80L) // in centesimi
                    .setCurrency("eur")
                    .setProduct(product.getId())
                    .build();

            Price price = Price.create(priceParams);

            // Checkout
            SessionCreateParams sessionParams = SessionCreateParams.builder()
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(price.getId())
                                    .setQuantity(1L)
                                    .build())
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://example.com/success")
                    .setCancelUrl("https://example.com/cancel")
                    .build();

            Session session = Session.create(sessionParams);

            logger.info("Prodotto creato: " + product.getId());
            logger.info("Prezzo creato: " + price.getId());

            return ResponseEntity.ok(Map.of("id", session.getId()));

        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Errore Stripe: " + e.getMessage()));
        }
    }

}