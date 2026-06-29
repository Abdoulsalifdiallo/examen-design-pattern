package sn.examen.designpattern.badwalletapi.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decore une WithdrawalCalculation en ajoutant des frais de 1% du montant,
 * plafonnes a 5000 CFA, sans modifier le calcul de base.
 */
public class CappedPercentageFeeDecorator implements WithdrawalCalculation {

    private static final BigDecimal RATE = new BigDecimal("0.01");
    private static final BigDecimal CAP = new BigDecimal("5000");

    private final WithdrawalCalculation delegate;

    public CappedPercentageFeeDecorator(WithdrawalCalculation delegate) {
        this.delegate = delegate;
    }

    @Override
    public BigDecimal fee(BigDecimal amount) {
        BigDecimal computedFee = amount.multiply(RATE).setScale(2, RoundingMode.HALF_UP);
        return computedFee.min(CAP);
    }

    @Override
    public BigDecimal totalDebit(BigDecimal amount) {
        return delegate.totalDebit(amount).add(fee(amount));
    }
}
