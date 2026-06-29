package sn.examen.designpattern.paymentservice.billing;

import sn.examen.designpattern.paymentservice.domain.ServiceName;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Factory Method : fournit la BillingRule (Strategy) correspondant au
 * service de facturation demande.
 */
@Component
public class BillingRuleFactory {

    private final Map<ServiceName, BillingRule> rules = new EnumMap<>(ServiceName.class);

    public BillingRuleFactory(List<BillingRule> availableRules) {
        availableRules.forEach(rule -> rules.put(rule.getServiceName(), rule));
    }

    public BillingRule forService(ServiceName serviceName) {
        BillingRule rule = rules.get(serviceName);
        if (rule == null) {
            throw new IllegalArgumentException("Aucune regle de facturation pour le service " + serviceName);
        }
        return rule;
    }
}
