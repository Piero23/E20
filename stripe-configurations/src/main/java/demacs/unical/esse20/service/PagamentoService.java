package demacs.unical.esse20.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PagamentoService {

    @Value("${stripe.api.secret}")
    private String stripeSecretKey;

    public Session createPaymentSession(String nomeArticolo, long prezzo, String valuta,
                                        String successUrl, String cancelUrl) throws StripeException {

        Objects.requireNonNull(nomeArticolo, "nomeArticolo non può essere null");
        Objects.requireNonNull(valuta, "valuta non può essere null");
        Objects.requireNonNull(successUrl, "successUrl non può essere null");
        Objects.requireNonNull(cancelUrl, "cancelUrl non può essere null");

        if (stripeSecretKey == null || stripeSecretKey.isBlank()) {
            throw new IllegalStateException("La chiave Stripe non è configurata. Imposta stripe.api.secret in application.properties");
        }

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(valuta)
                                                .setUnitAmount(prezzo)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(nomeArticolo)
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
}