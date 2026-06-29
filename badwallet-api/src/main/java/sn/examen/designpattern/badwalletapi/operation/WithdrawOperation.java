package sn.examen.designpattern.badwalletapi.operation;

import sn.examen.designpattern.badwalletapi.decorator.WithdrawalCalculation;
import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.domain.TransactionType;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.exception.InsufficientBalanceException;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;
import sn.examen.designpattern.badwalletapi.repository.TransactionRepository;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;

public class WithdrawOperation extends AbstractWalletOperation {

    private final Wallet wallet;
    private final BigDecimal amount;
    private final WithdrawalCalculation calculation;
    private final WalletRepository walletRepository;

    public WithdrawOperation(Wallet wallet, BigDecimal amount, WithdrawalCalculation calculation,
                              WalletRepository walletRepository, TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.wallet = wallet;
        this.amount = amount;
        this.calculation = calculation;
        this.walletRepository = walletRepository;
    }

    @Override
    protected void validate() {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidOperationException("Le montant du retrait doit etre superieur a zero");
        }
        BigDecimal totalDebit = calculation.totalDebit(amount);
        if (wallet.getBalance().compareTo(totalDebit) < 0) {
            throw new InsufficientBalanceException(
                    "Solde insuffisant pour retirer " + amount + " (frais inclus : " + totalDebit + ")");
        }
    }

    @Override
    protected List<Transaction> doExecute() {
        BigDecimal fee = calculation.fee(amount);
        BigDecimal totalDebit = calculation.totalDebit(amount);
        wallet.debit(totalDebit);
        walletRepository.save(wallet);
        Transaction transaction = new Transaction(
                wallet.getId(), TransactionType.WITHDRAW, amount, fee, null, null);
        return List.of(transaction);
    }
}
