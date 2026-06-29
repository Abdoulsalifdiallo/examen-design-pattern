package sn.examen.designpattern.badwalletapi.decorator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CappedPercentageFeeDecoratorTest {

    private final WithdrawalCalculation calculation = new CappedPercentageFeeDecorator(new BaseWithdrawalCalculation());

    @Test
    void feeIsOnePercentBelowCap() {
        BigDecimal fee = calculation.fee(new BigDecimal("10000"));

        assertEquals(new BigDecimal("100.00"), fee);
    }

    @Test
    void feeIsCappedAtFiveThousand() {
        BigDecimal fee = calculation.fee(new BigDecimal("1000000"));

        assertEquals(new BigDecimal("5000"), fee);
    }

    @Test
    void totalDebitIncludesAmountAndFee() {
        BigDecimal total = calculation.totalDebit(new BigDecimal("10000"));

        assertEquals(new BigDecimal("10100.00"), total);
    }
}
