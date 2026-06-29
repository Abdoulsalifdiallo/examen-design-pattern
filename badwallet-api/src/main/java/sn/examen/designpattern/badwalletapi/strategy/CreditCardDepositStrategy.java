package sn.examen.designpattern.badwalletapi.strategy;

import sn.examen.designpattern.badwalletapi.domain.PaymentMethod;
import sn.examen.designpattern.badwalletapi.domain.Wallet;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Simule un encaissement par carte bancaire externe : le montant est
 * directement credite, sans portefeuille source interne.
 */
@Component
public class CreditCardDepositStrategy implements DepositStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public void credit(Wallet wallet, BigDecimal amount) {
        wallet.credit(amount);
    }
}
