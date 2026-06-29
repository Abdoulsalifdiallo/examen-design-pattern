package sn.examen.designpattern.badwalletapi.service;

import sn.examen.designpattern.badwalletapi.decorator.BaseWithdrawalCalculation;
import sn.examen.designpattern.badwalletapi.decorator.CappedPercentageFeeDecorator;
import sn.examen.designpattern.badwalletapi.decorator.WithdrawalCalculation;
import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.dto.WalletCreateRequest;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;
import sn.examen.designpattern.badwalletapi.exception.WalletNotFoundException;
import sn.examen.designpattern.badwalletapi.gateway.BillPaymentGateway;
import sn.examen.designpattern.badwalletapi.gateway.PaymentReceipt;
import sn.examen.designpattern.badwalletapi.operation.DepositOperation;
import sn.examen.designpattern.badwalletapi.operation.PaymentRecordOperation;
import sn.examen.designpattern.badwalletapi.operation.TransferOperation;
import sn.examen.designpattern.badwalletapi.operation.WithdrawOperation;
import sn.examen.designpattern.badwalletapi.repository.TransactionRepository;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;
import sn.examen.designpattern.badwalletapi.strategy.DepositStrategyFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletCodeGenerator codeGenerator;
    private final DepositStrategyFactory depositStrategyFactory;
    private final BillPaymentGateway billPaymentGateway;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository,
                          WalletCodeGenerator codeGenerator, DepositStrategyFactory depositStrategyFactory,
                          BillPaymentGateway billPaymentGateway) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.codeGenerator = codeGenerator;
        this.depositStrategyFactory = depositStrategyFactory;
        this.billPaymentGateway = billPaymentGateway;
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
        wallet = walletRepository.save(wallet);
        initializeFacturesBestEffort(wallet.getCode());
        return wallet;
    }

    /**
     * L'initialisation des factures chez payment-service est une consequence
     * de la creation du wallet, pas une condition de celle-ci : si
     * payment-service est indisponible, le wallet reste cree.
     */
    private void initializeFacturesBestEffort(String walletCode) {
        try {
            billPaymentGateway.initializeFactures(walletCode);
        } catch (RuntimeException ex) {
            log.warn("Initialisation des factures impossible pour {} : {}", walletCode, ex.getMessage());
        }
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

    public List<Transaction> transfer(String senderPhone, String receiverPhone, BigDecimal amount) {
        Wallet sender = getByPhone(senderPhone);
        Wallet receiver = getByPhone(receiverPhone);
        return new TransferOperation(sender, receiver, amount, walletRepository, transactionRepository).execute();
    }

    public List<Transaction> payCurrentMonthBill(String phoneNumber, String serviceName, BigDecimal amount) {
        Wallet wallet = getByPhone(phoneNumber);
        PaymentReceipt receipt = billPaymentGateway.payCurrentMonth(wallet.getCode(), serviceName, amount);
        return new PaymentRecordOperation(wallet, receipt.getAmountCharged(), serviceName, walletRepository, transactionRepository)
                .execute();
    }

    public List<Transaction> payFactures(String phoneNumber, String serviceName, List<String> factureReferences) {
        Wallet wallet = getByPhone(phoneNumber);
        PaymentReceipt receipt = billPaymentGateway.payByReferences(wallet.getCode(), serviceName, factureReferences);
        return new PaymentRecordOperation(wallet, receipt.getAmountCharged(), serviceName, walletRepository, transactionRepository)
                .execute();
    }

    public List<Transaction> getTransactions(String phoneNumber) {
        Wallet wallet = getByPhone(phoneNumber);
        return transactionRepository.findByWalletIdOrderByTimestampDesc(wallet.getId());
    }
}
