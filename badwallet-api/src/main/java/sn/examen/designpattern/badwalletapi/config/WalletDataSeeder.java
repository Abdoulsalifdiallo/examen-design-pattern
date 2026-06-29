package sn.examen.designpattern.badwalletapi.config;

import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;
import sn.examen.designpattern.badwalletapi.service.WalletCodeGenerator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cree quelques portefeuilles par defaut (+221770000001 a 005) au demarrage
 * pour que les exemples du sujet (WLT-0000003, +221770000003...) fonctionnent
 * sans devoir d'abord appeler /api/wallets/seed.
 */
@Component
public class WalletDataSeeder implements CommandLineRunner {

    private static final int DEFAULT_WALLET_COUNT = 5;

    private final WalletRepository walletRepository;

    public WalletDataSeeder(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public void run(String... args) {
        if (walletRepository.count() > 0) {
            return;
        }
        for (long n = 1; n <= DEFAULT_WALLET_COUNT; n++) {
            Wallet wallet = Wallet.builder()
                    .code(WalletCodeGenerator.format(n))
                    .phoneNumber(WalletCodeGenerator.formatPhone(n))
                    .email("client" + n + "@badwallet.sn")
                    .balance(BigDecimal.valueOf(50000))
                    .currency("XOF")
                    .build();
            walletRepository.save(wallet);
        }
    }
}
