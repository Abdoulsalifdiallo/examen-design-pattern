package sn.examen.designpattern.badwalletapi.proxy;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface representant le fournisseur de factures, que le client
 * (controleur) traite comme une ressource locale. PARTIE 2 du sujet :
 * "Proxy API dans badwallet-api".
 */
public interface FactureProvider {

    List<FactureView> getCurrentMonthFactures(String walletCode, String unite);

    List<FactureView> getFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin);
}
