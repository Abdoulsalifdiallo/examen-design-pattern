package sn.examen.designpattern.badwalletapi.service;

import sn.examen.designpattern.badwalletapi.domain.PaymentMethod;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.dto.WalletCreateRequest;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genere des portefeuilles et un historique de transactions aleatoire en
 * tache de fond, pour que l'appel POST /api/wallets/seed reponde
 * immediatement (202 Accepted).
 */
@Service
public class WalletSeederService {

    private static final Logger log = LoggerFactory.getLogger(WalletSeederService.class);

    private final WalletService walletService;
    private final WalletRepository walletRepository;
    private final Random random = new Random();

    public WalletSeederService(WalletService walletService, WalletRepository walletRepository) {
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    @Async
    public void seed(int numWallets, int eventsPerWallet) {
        log.info("Demarrage du seed asynchrone : {} portefeuilles, {} evenements chacun", numWallets, eventsPerWallet);
        List<Wallet> created = new ArrayList<>();
        for (int i = 0; i < numWallets; i++) {
            created.add(createRandomWallet());
        }
        for (Wallet wallet : created) {
            for (int e = 0; e < eventsPerWallet; e++) {
                runRandomEvent(wallet, created);
            }
        }
        log.info("Seed asynchrone termine : {} portefeuilles crees", created.size());
    }

    private Wallet createRandomWallet() {
        long n = walletRepository.count() + 1;
        WalletCreateRequest request = new WalletCreateRequest();
        request.setPhoneNumber(WalletCodeGenerator.formatPhone(n));
        request.setEmail("client" + n + "@badwallet.sn");
        request.setInitialBalance(BigDecimal.valueOf(5000 + random.nextInt(95000)));
        request.setCurrency("XOF");
        return walletService.createWallet(request);
    }

    private void runRandomEvent(Wallet wallet, List<Wallet> pool) {
        try {
            int action = random.nextInt(3);
            BigDecimal amount = BigDecimal.valueOf(500 + random.nextInt(20000));
            switch (action) {
                case 0 -> walletService.deposit(wallet.getId(), amount, PaymentMethod.CREDIT_CARD);
                case 1 -> walletService.withdraw(wallet.getPhoneNumber(), amount);
                default -> {
                    Wallet other = pool.get(random.nextInt(pool.size()));
                    if (!other.getId().equals(wallet.getId())) {
                        walletService.transfer(wallet.getPhoneNumber(), other.getPhoneNumber(), amount);
                    }
                }
            }
        } catch (RuntimeException ex) {
            log.debug("Evenement de seed ignore pour {} : {}", wallet.getPhoneNumber(), ex.getMessage());
        }
    }
}
