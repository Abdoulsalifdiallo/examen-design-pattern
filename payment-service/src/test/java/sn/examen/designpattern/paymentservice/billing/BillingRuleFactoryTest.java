package sn.examen.designpattern.paymentservice.billing;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BillingRuleFactoryTest {

    private final BillingRuleFactory factory = new BillingRuleFactory(
            List.of(new IsmBillingRule(), new WoyafalBillingRule()));

    @Test
    void ismAppliesTwoPercentPenaltyWhenOverdue() {
        Facture facture = new Facture("FAC-ISM-1-1", "WLT-0000001", ServiceName.ISM,
                new BigDecimal("1000"), 1, 2026, LocalDate.now().minusDays(1), false);

        BigDecimal due = factory.forService(ServiceName.ISM).computeAmountDue(facture);

        assertEquals(new BigDecimal("1020.00"), due);
    }

    @Test
    void woyafalAppliesFlatLateFeeWhenOverdue() {
        Facture facture = new Facture("FAC-WOYAFAL-1-1", "WLT-0000001", ServiceName.WOYAFAL,
                new BigDecimal("1000"), 1, 2026, LocalDate.now().minusDays(1), false);

        BigDecimal due = factory.forService(ServiceName.WOYAFAL).computeAmountDue(facture);

        assertEquals(new BigDecimal("1100"), due);
    }

    @Test
    void noPenaltyWhenNotOverdue() {
        Facture facture = new Facture("FAC-ISM-1-2", "WLT-0000001", ServiceName.ISM,
                new BigDecimal("1000"), 1, 2026, LocalDate.now().plusDays(5), false);

        BigDecimal due = factory.forService(ServiceName.ISM).computeAmountDue(facture);

        assertEquals(new BigDecimal("1000"), due);
    }

    @Test
    void unknownServiceThrows() {
        BillingRuleFactory emptyFactory = new BillingRuleFactory(List.of());

        assertThrows(IllegalArgumentException.class, () -> emptyFactory.forService(ServiceName.ISM));
    }
}
