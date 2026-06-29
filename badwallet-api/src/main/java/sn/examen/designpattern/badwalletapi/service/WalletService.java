package sn.examen.designpattern.badwalletapi.service;

import sn.examen.designpattern.badwalletapi.decorator.BaseWithdrawalCalculation;
import sn.examen.designpattern.badwalletapi.decorator.CappedPercentageFeeDecorator;
import sn.examen.designpattern.badwalletapi.decorator.WithdrawalCalculation;
import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.dto.WalletCreateRequest;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;
import sn.examen.designpattern.badwalletapi.exception.WalletNotFoundException;
import sn.examen.designpattern.badwalletapi.operation.DepositOperation;
import sn.examen.designpattern.badwalletapi.operation.WithdrawOperation;
import sn.examen.designpattern.badwalletapi.repository.TransactionRepository;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;
import sn.examen.designpattern.badwalletapi.strategy.DepositStrategyFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletCodeGenerator codeGenerator;
    private final DepositStrategyFactory depositStrategyFactory;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository,
                          WalletCodeGenerator codeGenerator, DepositStrategyFactory depositStrategyFactory) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.codeGenerator = codeGenerator;
        this.depositStrategyFactory = depositStrategyFactory;
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

    public Wallet getByPhone(String phoneNumber) {
        return walletRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new WalletNotFoundException("Aucun portefeuille pour " + phoneNumber));
    }

    public Wallet getById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Aucun portefeuille avec l'id " + id));
    }

    public List<Transaction> deposit(Long walletId, BigDecimal amount, sn.examen.designpattern.badwalletapi.domain.PaymentMethod paymentMethod) {
        Wallet wallet = getById(walletId);
        var strategy = depositStrategyFactory.forPaymentMethod(paymentMethod);
        return new DepositOperation(wallet, amount, paymentMethod, strategy, walletRepository, transactionRepository)
                .execute();
    }

    public List<Transaction> withdraw(String phoneNumber, BigDecimal amount) {
        Wallet wallet = getByPhone(phoneNumber);
        WithdrawalCalculation calculation = new CappedPercentageFeeDecorator(new BaseWithdrawalCalculation());
        return new WithdrawOperation(wallet, amount, calculation, walletRepository, transactionRepository).execute();
    }
}
