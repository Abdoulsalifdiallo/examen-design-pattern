package sn.examen.designpattern.paymentservice.config;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;
import sn.examen.designpattern.paymentservice.repository.FactureRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Peuple des factures de demonstration pour 10 portefeuilles (WLT-0000001 a
 * WLT-0000010), avec les references citees en exemple dans le sujet
 * (ex: FAC-ISM-3-1, FAC-ISM-3-3 pour WLT-0000003).
 */
@Component
public class FactureDataSeeder implements CommandLineRunner {

    private static final int WALLET_COUNT = 10;
    private static final int FACTURES_PER_SERVICE = 3;

    private final FactureRepository factureRepository;

    public FactureDataSeeder(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Override
    public void run(String... args) {
        if (factureRepository.count() > 0) {
            return;
        }
        LocalDate now = LocalDate.now();
        LocalDate dateEcheance = now.withDayOfMonth(now.lengthOfMonth());

        for (int walletNum = 1; walletNum <= WALLET_COUNT; walletNum++) {
            String walletCode = String.format("WLT-%07d", walletNum);
            seedService(walletCode, walletNum, ServiceName.ISM, dateEcheance);
            seedService(walletCode, walletNum, ServiceName.WOYAFAL, dateEcheance);
        }
    }

    private void seedService(String walletCode, int walletNum, ServiceName serviceName, LocalDate dateEcheance) {
        for (int seq = 1; seq <= FACTURES_PER_SERVICE; seq++) {
            String reference = "FAC-" + serviceName + "-" + walletNum + "-" + seq;
            BigDecimal montant = BigDecimal.valueOf(3000 + (seq * 1500));
            factureRepository.save(new Facture(
                    reference, walletCode, serviceName, montant,
                    dateEcheance.getMonthValue(), dateEcheance.getYear(), dateEcheance, false));
        }
    }
}
