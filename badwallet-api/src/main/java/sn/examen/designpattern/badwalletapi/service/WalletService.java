package sn.examen.designpattern.badwalletapi.service;

import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.dto.WalletCreateRequest;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletCodeGenerator codeGenerator;

    public WalletService(WalletRepository walletRepository, WalletCodeGenerator codeGenerator) {
        this.walletRepository = walletRepository;
        this.codeGenerator = codeGenerator;
    }

    public Wallet createWallet(WalletCreateRequest request) {
        if (walletRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new InvalidOperationException("Un portefeuille existe deja pour " + request.getPhoneNumber());
        }
        String code = (request.getCode() == null || request.getCode().isBlank())
                ? codeGenerator.generate()
                : request.getCode();
        if (walletRepository.existsByCode(code)) {
            throw new InvalidOperationException("Le code de portefeuille " + code + " est deja utilise");
        }
        Wallet wallet = Wallet.builder()
                .code(code)
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .balance(request.getInitialBalance())
                .currency(request.getCurrency())
                .build();
        return walletRepository.save(wallet);
    }

    public Page<Wallet> listWallets(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }
}
