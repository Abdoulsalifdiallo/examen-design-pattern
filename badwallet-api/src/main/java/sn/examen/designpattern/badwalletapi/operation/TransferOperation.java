package sn.examen.designpattern.badwalletapi.operation;

import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.domain.TransactionType;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.exception.InsufficientBalanceException;
import sn.examen.designpattern.badwalletapi.exception.InvalidOperationException;
import sn.examen.designpattern.badwalletapi.repository.TransactionRepository;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;

public class TransferOperation extends AbstractWalletOperation {

    private final Wallet sender;
    private final Wallet receiver;
    private final BigDecimal amount;
    private final WalletRepository walletRepository;

    public TransferOperation(Wallet sender, Wallet receiver, BigDecimal amount,
                              WalletRepository walletRepository, TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.walletRepository = walletRepository;
    }

    @Override
    protected void validate() {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidOperationException("Le montant du transfert doit etre superieur a zero");
        }
        if (sender.getId().equals(receiver.getId())) {
            throw new InvalidOperationException("Impossible de transferer vers son propre portefeuille");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Solde insuffisant pour transferer " + amount);
        }
    }

    @Override
    protected List<Transaction> doExecute() {
        sender.debit(amount);
        receiver.credit(amount);
        walletRepository.save(sender);
        walletRepository.save(receiver);

        Transaction out = new Transaction(
                sender.getId(), TransactionType.TRANSFER_OUT, amount, BigDecimal.ZERO, null, receiver.getPhoneNumber());
        Transaction in = new Transaction(
                receiver.getId(), TransactionType.TRANSFER_IN, amount, BigDecimal.ZERO, null, sender.getPhoneNumber());
        return List.of(out, in);
    }
}
