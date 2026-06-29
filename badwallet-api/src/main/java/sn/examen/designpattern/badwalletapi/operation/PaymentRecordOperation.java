package sn.examen.designpattern.badwalletapi.operation;

import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.domain.TransactionType;
import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.exception.InsufficientBalanceException;
import sn.examen.designpattern.badwalletapi.repository.TransactionRepository;
import sn.examen.designpattern.badwalletapi.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Debite localement le portefeuille une fois que le paiement de facture a
 * ete confirme par payment-service (via l'Adapter).
 */
public class PaymentRecordOperation extends AbstractWalletOperation {

    private final Wallet wallet;
    private final BigDecimal amountCharged;
    private final String serviceName;
    private final WalletRepository walletRepository;

    public PaymentRecordOperation(Wallet wallet, BigDecimal amountCharged, String serviceName,
                                   WalletRepository walletRepository, TransactionRepository transactionRepository) {
        super(transactionRepository);
        this.wallet = wallet;
        this.amountCharged = amountCharged;
        this.serviceName = serviceName;
        this.walletRepository = walletRepository;
    }

    @Override
    protected void validate() {
        if (wallet.getBalance().compareTo(amountCharged) < 0) {
            throw new InsufficientBalanceException(
                    "Solde insuffisant pour regler la facture " + serviceName + " (" + amountCharged + ")");
        }
    }

    @Override
    protected List<Transaction> doExecute() {
        wallet.debit(amountCharged);
        walletRepository.save(wallet);
        Transaction transaction = new Transaction(
                wallet.getId(), TransactionType.PAYMENT, amountCharged, BigDecimal.ZERO, null, serviceName);
        return List.of(transaction);
    }
}
