package sn.examen.designpattern.paymentservice.billing;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;

import java.math.BigDecimal;

/**
 * Strategy : chaque service de facturation (ISM, WOYAFAL) calcule
 * differemment le montant reellement du (penalites de retard incluses).
 */
public interface BillingRule {

    ServiceName getServiceName();

    BigDecimal computeAmountDue(Facture facture);
}
