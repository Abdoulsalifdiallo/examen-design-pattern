package sn.examen.designpattern.badwalletapi.dto;

import sn.examen.designpattern.badwalletapi.domain.Transaction;
import sn.examen.designpattern.badwalletapi.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponse {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal fee;
    private String paymentMethod;
    private String counterparty;
    private Instant timestamp;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.fee = transaction.getFee();
        this.paymentMethod = transaction.getPaymentMethod();
        this.counterparty = transaction.getCounterparty();
        this.timestamp = transaction.getTimestamp();
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
