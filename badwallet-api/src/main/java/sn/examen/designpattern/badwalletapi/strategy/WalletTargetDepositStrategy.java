package sn.examen.designpattern.badwalletapi.strategy;

import sn.examen.designpattern.badwalletapi.domain.PaymentMethod;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Alimentation depuis le reseau interne de portefeuilles : plafonnee, a la
 * difference d'un encaissement carte bancaire qui n'a pas de limite.
 */
@Component
public class WalletTargetDepositStrategy implements DepositStrategy {

    private static final BigDecimal MAX_INTERNAL_DEPOSIT = new BigDecimal("1000000");

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.WALLET_TARGET;
    }

    @Override
    public void credit(Wallet wallet, BigDecimal amount) {
        if (amount.compareTo(MAX_INTERNAL_DEPOSIT) > 0) {
            throw new InvalidOperationException(
                    "Le depot via le reseau interne de portefeuilles est plafonne a " + MAX_INTERNAL_DEPOSIT);
        }
        wallet.credit(amount);
    }
}
