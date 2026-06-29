package sn.examen.designpattern.badwalletapi.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

/**
 * Reflet brut du JSON renvoye par payment-service (POST /api/factures/pay),
 * traduit ensuite vers PaymentReceipt par l'Adapter.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawPaymentReceipt {

    private BigDecimal amountCharged;
    private List<String> paidReferences;

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(BigDecimal amountCharged) {
        this.amountCharged = amountCharged;
    }

    public List<String> getPaidReferences() {
        return paidReferences;
    }

    public void setPaidReferences(List<String> paidReferences) {
        this.paidReferences = paidReferences;
    }
}
