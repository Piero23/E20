package demacs.unical.esse20;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentMethodCreateParams;

import java.util.Scanner;

public class StripePaymentTest {

    public static void main(String[] args) {
        Stripe.apiKey = "sk_test_51RFjSkP6wuNuFoXfwTOPhTkziWj1DDXYpCwLLkxKQFAZaojXuPeh8FX5DHOMvPjvmTgMkoMhPGNa9oIrGoKYO7Sb00ZNYIN3FZ";

        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci l'importo in EUR (es. 10.50):");
        double amountEur = Double.parseDouble(scanner.nextLine());
        long amountInCents = (long) (amountEur * 100);

        try {
            PaymentMethodCreateParams.CardDetails card =
                    PaymentMethodCreateParams.CardDetails.builder()
                            .setNumber("4242424242424242")
                            .setExpMonth(12L)
                            .setExpYear(2030L)
                            .setCvc("123")
                            .build();

            PaymentMethodCreateParams paymentMethodParams =
                    PaymentMethodCreateParams.builder()
                            .setType(PaymentMethodCreateParams.Type.CARD)
                            .setCard(card)
                            .build();

            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);

            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("eur")
                    .addPaymentMethodType("card")
                    .setPaymentMethod(paymentMethod.getId())
                    .setConfirm(true)  // lo conferma subito
                    .build();

            PaymentIntent intent = PaymentIntent.create(createParams);

            System.out.println("✅ Pagamento riuscito!");
            System.out.println("ID PaymentIntent: " + intent.getId());
            System.out.println("Stato: " + intent.getStatus());

        } catch (StripeException e) {
            System.err.println("❌ Errore Stripe: " + e.getMessage());
        }
    }
}