package sn.examen.designpattern.badwalletapi.strategy;

import sn.examen.designpattern.badwalletapi.domain.PaymentMethod;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DepositStrategyFactoryTest {

    private final DepositStrategyFactory factory = new DepositStrategyFactory(
            List.of(new CreditCardDepositStrategy(), new WalletTargetDepositStrategy()));

    @Test
    void creditCardStrategyCreditsWithoutLimit() {
        Wallet wallet = Wallet.builder().code("WLT-0000001").phoneNumber("+221770000001")
                .email("a@a.sn").balance(BigDecimal.ZERO).build();

        factory.forPaymentMethod(PaymentMethod.CREDIT_CARD).credit(wallet, new BigDecimal("2000000"));

        assertEquals(new BigDecimal("2000000"), wallet.getBalance());
    }

    @Test
    void walletTargetStrategyRejectsAboveCap() {
        Wallet wallet = Wallet.builder().code("WLT-0000002").phoneNumber("+221770000002")
                .email("b@b.sn").balance(BigDecimal.ZERO).build();

        assertThrows(InvalidOperationException.class,
                () -> factory.forPaymentMethod(PaymentMethod.WALLET_TARGET).credit(wallet, new BigDecimal("2000000")));
    }
}
