package sn.examen.designpattern.badwalletapi.operation;

import sn.examen.designpattern.badwalletapi.domain.PaymentMethod;
import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.domain.TransactionType;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;
import sn.examen.designpattern.badwalletapi.repository.TransactionRepository;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;
import sn.examen.designpattern.badwalletapi.strategy.DepositStrategy;

import java.math.BigDecimal;
import java.util.List;

public class DepositOperation extends AbstractWalletOperation {

    private final Wallet wallet;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private final DepositStrategy strategy;
    private final WalletRepository walletRepository;

    public DepositOperation(Wallet wallet, BigDecimal amount, PaymentMethod paymentMethod, DepositStrategy strategy,
                             WalletRepository walletRepository, TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.wallet = wallet;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.strategy = strategy;
        this.walletRepository = walletRepository;
    }

    @Override
    protected void validate() {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidOperationException("Le montant du depot doit etre superieur a zero");
        }
    }

    @Override
    protected List<Transaction> doExecute() {
        strategy.credit(wallet, amount);
        walletRepository.save(wallet);
        Transaction transaction = new Transaction(
                wallet.getId(), TransactionType.DEPOSIT, amount, BigDecimal.ZERO, paymentMethod.name(), null);
        return List.of(transaction);
    }
}
