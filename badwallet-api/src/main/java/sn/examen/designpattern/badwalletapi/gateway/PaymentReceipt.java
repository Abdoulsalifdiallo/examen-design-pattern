package sn.examen.designpattern.badwalletapi.gateway;

import java.math.BigDecimal;
import java.util.List;

public class PaymentReceipt {

    private final BigDecimal amountCharged;
    private final List<String> paidReferences;

    public PaymentReceipt(BigDecimal amountCharged, List<String> paidReferences) {
        this.amountCharged = amountCharged;
        this.paidReferences = paidReferences;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public List<String> getPaidReferences() {
        return paidReferences;
    }
}
