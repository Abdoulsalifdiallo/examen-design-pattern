package sn.examen.designpattern.badwalletapi.strategy;

import sn.examen.designpattern.badwalletapi.domain.PaymentMethod;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class DepositStrategyFactory {

    private final Map<PaymentMethod, DepositStrategy> strategies = new EnumMap<>(PaymentMethod.class);

    public DepositStrategyFactory(List<DepositStrategy> availableStrategies) {
        availableStrategies.forEach(strategy -> strategies.put(strategy.getPaymentMethod(), strategy));
    }

    public DepositStrategy forPaymentMethod(PaymentMethod paymentMethod) {
        DepositStrategy strategy = strategies.get(paymentMethod);
        if (strategy == null) {
            throw new IllegalArgumentException("Moyen de paiement non supporte : " + paymentMethod);
        }
        return strategy;
    }
}
