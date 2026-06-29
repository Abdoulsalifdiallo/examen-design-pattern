package sn.examen.designpattern.paymentservice.config;

import sn.examen.designpattern.paymentservice.repository.FactureRepository;
import sn.examen.designpattern.paymentservice.service.FactureInitializationService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialise les factures de 10 portefeuilles de demonstration
 * (WLT-0000001 a WLT-0000010) au demarrage, avec les references citees en
 * exemple dans le sujet (ex: FAC-ISM-3-1, FAC-ISM-3-3 pour WLT-0000003).
 */
@Component
public class FactureDataSeeder implements CommandLineRunner {

    private static final int WALLET_COUNT = 10;

    private final FactureInitializationService initializationService;
    private final FactureRepository factureRepository;

    public FactureDataSeeder(FactureInitializationService initializationService, FactureRepository factureRepository) {
        this.initializationService = initializationService;
        this.factureRepository = factureRepository;
    }

    @Override
    public void run(String... args) {
        if (factureRepository.count() > 0) {
            return;
        }
        for (int walletNum = 1; walletNum <= WALLET_COUNT; walletNum++) {
            initializationService.initializeFor(String.format("WLT-%07d", walletNum));
        }
    }
}
