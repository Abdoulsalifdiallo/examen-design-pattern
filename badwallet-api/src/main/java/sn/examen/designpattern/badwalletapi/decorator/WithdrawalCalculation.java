package sn.examen.designpattern.badwalletapi.decorator;

import java.math.BigDecimal;

/**
 * Decorator : calcule les frais et le montant total a debiter pour un
 * retrait. La version de base ne prelève aucun frais ; des decorateurs
 * peuvent s'empiler pour ajouter des regles de frais sans la modifier.
 */
public interface WithdrawalCalculation {

    BigDecimal fee(BigDecimal amount);

    BigDecimal totalDebit(BigDecimal amount);
}
