package sn.examen.designpattern.badwalletapi.operation;

import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.repository.TransactionRepository;

import java.util.List;

/**
 * Template Method : toute operation de portefeuille suit le meme squelette
 * (valider, executer, enregistrer l'historique). Les sous-classes
 * (depot, retrait, transfert, paiement) ne redefinissent que les etapes
 * specifiques.
 */
public abstract class AbstractWalletOperation {

    private final TransactionRepository transactionRepository;

    protected AbstractWalletOperation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public final List<Transaction> execute() {
        validate();
        List<Transaction> transactions = doExecute();
        transactions.forEach(transactionRepository::save);
        return transactions;
    }

    protected abstract void validate();

    protected abstract List<Transaction> doExecute();
}
