package sn.examen.designpattern.paymentservice.service;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;
import sn.examen.designpattern.paymentservice.repository.FactureRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialise les factures mensuelles (ISM, WOYAFAL) d'un portefeuille la
 * premiere fois qu'il est connu de payment-service. Idempotent : si des
 * factures existent deja pour ce walletCode, l'appel est sans effet.
 */
@Service
public class FactureInitializationService {

    private static final int FACTURES_PER_SERVICE = 3;

    private final FactureRepository factureRepository;

    public FactureInitializationService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    public List<Facture> initializeFor(String walletCode) {
        if (factureRepository.existsByWalletCode(walletCode)) {
            return List.of();
        }
        String identifier = walletIdentifier(walletCode);
        LocalDate dateEcheance = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<Facture> created = new ArrayList<>();
        created.addAll(seedService(walletCode, identifier, ServiceName.ISM, dateEcheance));
        created.addAll(seedService(walletCode, identifier, ServiceName.WOYAFAL, dateEcheance));
        return created;
    }

    private List<Facture> seedService(String walletCode, String identifier, ServiceName serviceName,
                                       LocalDate dateEcheance) {
        List<Facture> factures = new ArrayList<>();
        for (int seq = 1; seq <= FACTURES_PER_SERVICE; seq++) {
            String reference = "FAC-" + serviceName + "-" + identifier + "-" + seq;
            BigDecimal montant = BigDecimal.valueOf(3000 + (seq * 1500));
            factures.add(factureRepository.save(new Facture(
                    reference, walletCode, serviceName, montant,
                    dateEcheance.getMonthValue(), dateEcheance.getYear(), dateEcheance, false)));
        }
        return factures;
    }

    private String walletIdentifier(String walletCode) {
        String suffix = walletCode.startsWith("WLT-") ? walletCode.substring(4) : walletCode;
        return suffix.matches("\\d+") ? String.valueOf(Long.parseLong(suffix)) : suffix;
    }
}
