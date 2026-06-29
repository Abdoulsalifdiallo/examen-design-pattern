package sn.examen.designpattern.badwalletapi.gateway;

import sn.examen.designpattern.badwalletapi.exception.ExternalServiceException;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;
import sn.examen.designpattern.badwalletapi.exception.WalletNotFoundException;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Adapter : traduit l'interface metier locale BillPaymentGateway en appels
 * HTTP vers l'API REST externe de payment-service, et adapte ses reponses /
 * erreurs au modele d'exceptions de badwallet-api.
 */
@Component
public class PaymentServiceAdapter implements BillPaymentGateway {

    private final RestClient paymentServiceClient;

    public PaymentServiceAdapter(RestClient paymentServiceClient) {
        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    public void initializeFactures(String walletCode) {
        try {
            paymentServiceClient.post()
                    .uri("/api/factures/init")
                    .body(Map.of("walletCode", walletCode))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("payment-service est inaccessible : " + ex.getMessage());
        }
    }

    @Override
    public PaymentReceipt payCurrentMonth(String walletCode, String serviceName, BigDecimal amount) {
        return call(Map.of(
                "walletCode", walletCode,
                "serviceName", serviceName,
                "amount", amount
        ));
    }

    @Override
    public PaymentReceipt payByReferences(String walletCode, String serviceName, List<String> factureReferences) {
        return call(Map.of(
                "walletCode", walletCode,
                "serviceName", serviceName,
                "factureReferences", factureReferences
        ));
    }

    private PaymentReceipt call(Map<String, Object> body) {
        try {
            RawPaymentReceipt raw = paymentServiceClient.post()
                    .uri("/api/factures/pay")
                    .body(body)
                    .retrieve()
                    .body(RawPaymentReceipt.class);
            return new PaymentReceipt(raw.getAmountCharged(), raw.getPaidReferences());
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("payment-service est inaccessible : " + ex.getMessage());
        }
    }

    private RuntimeException translate(RestClientResponseException ex) {
        HttpStatusCode status = ex.getStatusCode();
        String message = "payment-service a refuse le paiement : " + ex.getStatusText();
        if (status.value() == 404) {
            return new WalletNotFoundException(message);
        }
        if (status.value() == 400) {
            return new InvalidOperationException(message);
        }
        return new ExternalServiceException(message);
    }
}
