package sn.examen.designpattern.badwalletapi.gateway;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface metier locale (badwallet-api) pour le paiement de factures,
 * independante du protocole reellement utilise pour joindre payment-service.
 */
public interface BillPaymentGateway {

    PaymentReceipt payCurrentMonth(String walletCode, String serviceName, BigDecimal amount);

    PaymentReceipt payByReferences(String walletCode, String serviceName, List<String> factureReferences);
}
