package sn.examen.designpattern.paymentservice.billing;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * ISM applique une penalite de retard de 2% si la date d'echeance est depassee.
 */
@Component
public class IsmBillingRule implements BillingRule {

    private static final BigDecimal LATE_PENALTY_RATE = new BigDecimal("0.02");

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ISM;
    }

    @Override
    public BigDecimal computeAmountDue(Facture facture) {
        BigDecimal montant = facture.getMontant();
        if (facture.getDateEcheance().isBefore(LocalDate.now())) {
            BigDecimal penalty = montant.multiply(LATE_PENALTY_RATE).setScale(2, RoundingMode.HALF_UP);
            return montant.add(penalty);
        }
        return montant;
    }
}
