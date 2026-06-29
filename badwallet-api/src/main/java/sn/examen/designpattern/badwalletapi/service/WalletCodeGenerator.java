package sn.examen.designpattern.badwalletapi.service;

import sn.examen.designpattern.badwalletapi.repository.WalletRepository;

import org.springframework.stereotype.Component;

@Component
public class WalletCodeGenerator {

    private final WalletRepository walletRepository;

    public WalletCodeGenerator(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public synchronized String generate() {
        long candidate = walletRepository.count() + 1;
        String code;
        do {
            code = format(candidate++);
        } while (walletRepository.existsByCode(code));
        return code;
    }

    public static String format(long n) {
        return String.format("WLT-%07d", n);
    }

    public static String formatPhone(long n) {
        return String.format("+22177%07d", n);
    }
}
