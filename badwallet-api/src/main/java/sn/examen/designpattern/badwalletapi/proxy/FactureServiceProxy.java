package sn.examen.designpattern.badwalletapi.proxy;

import sn.examen.designpattern.badwalletapi.exception.ExternalServiceException;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDate;
import java.util.List;

/**
 * Proxy distant : expose la meme interface qu'un fournisseur local de
 * factures (FactureProvider) mais delegue chaque appel a payment-service via
 * HTTP, en ajoutant logging et traduction des erreurs.
 */
@Component
public class FactureServiceProxy implements FactureProvider {

    private static final Logger log = LoggerFactory.getLogger(FactureServiceProxy.class);

    private final RestClient paymentServiceClient;

    public FactureServiceProxy(RestClient paymentServiceClient) {
        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    public List<FactureView> getCurrentMonthFactures(String walletCode, String unite) {
        log.info("Proxy -> payment-service : factures courantes de {} (unite={})", walletCode, unite);
        try {
            FactureView[] result = unite == null
                    ? paymentServiceClient.get().uri("/api/factures/{code}/current", walletCode)
                        .retrieve().body(FactureView[].class)
                    : paymentServiceClient.get().uri("/api/factures/{code}/current?unite={unite}", walletCode, unite)
                        .retrieve().body(FactureView[].class);
            return result == null ? List.of() : List.of(result);
        } catch (RestClientResponseException ex) {
            throw translateClientError(ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("payment-service est inaccessible : " + ex.getMessage());
        }
    }

    @Override
    public List<FactureView> getFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
        log.info("Proxy -> payment-service : factures de {} entre {} et {}", walletCode, debut, fin);
        try {
            FactureView[] result = paymentServiceClient.get()
                    .uri("/api/factures/{code}/periode?debut={debut}&fin={fin}", walletCode, debut, fin)
                    .retrieve()
                    .body(FactureView[].class);
            return result == null ? List.of() : List.of(result);
        } catch (RestClientResponseException ex) {
            throw translateClientError(ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("payment-service est inaccessible : " + ex.getMessage());
        }
    }

    private RuntimeException translateClientError(RestClientResponseException ex) {
        if (ex.getStatusCode().value() == 400) {
            return new InvalidOperationException("Requete invalide vers payment-service : " + ex.getStatusText());
        }
        return new ExternalServiceException("payment-service a renvoye une erreur : " + ex.getStatusText());
    }
}
