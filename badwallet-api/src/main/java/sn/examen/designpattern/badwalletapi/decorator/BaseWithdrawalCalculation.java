package sn.examen.designpattern.badwalletapi.decorator;

import java.math.BigDecimal;

public class BaseWithdrawalCalculation implements WithdrawalCalculation {

    @Override
    public BigDecimal fee(BigDecimal amount) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal totalDebit(BigDecimal amount) {
        return amount;
    }
}
