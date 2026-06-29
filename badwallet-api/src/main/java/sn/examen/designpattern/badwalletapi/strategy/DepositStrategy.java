package sn.examen.designpattern.badwalletapi.strategy;

import sn.examen.designpattern.badwalletapi.domain.PaymentMethod;
import sn.examen.designpattern.badwalletapi.domain.Wallet;

import java.math.BigDecimal;

/**
 * Strategy : chaque moyen de paiement credite le portefeuille selon sa
 * propre logique (carte bancaire externe vs portefeuille interne).
 */
public interface DepositStrategy {

    PaymentMethod getPaymentMethod();

    void credit(Wallet wallet, BigDecimal amount);
}
