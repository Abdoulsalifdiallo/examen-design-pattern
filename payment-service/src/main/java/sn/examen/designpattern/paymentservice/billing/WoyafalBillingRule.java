package sn.examen.designpattern.paymentservice.billing;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * WOYAFAL applique des frais fixes de retard de 100 CFA si la date
 * d'echeance est depassee (regle differente de l'ISM).
 */
@Component
public class WoyafalBillingRule implements BillingRule {

    private static final BigDecimal LATE_FLAT_FEE = new BigDecimal("100");

    @Override
    public ServiceName getServiceName() {
        return ServiceName.WOYAFAL;
    }

    @Override
    public BigDecimal computeAmountDue(Facture facture) {
        BigDecimal montant = facture.getMontant();
        if (facture.getDateEcheance().isBefore(LocalDate.now())) {
            return montant.add(LATE_FLAT_FEE);
        }
        return montant;
    }
}
