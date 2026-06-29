package sn.examen.designpattern.badwalletapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal fee;

    private String paymentMethod;

    private String counterparty;

    @Column(nullable = false)
    private Instant timestamp;

    protected Transaction() {
    }

    public Transaction(Long walletId, TransactionType type, BigDecimal amount, BigDecimal fee,
                        String paymentMethod, String counterparty) {
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.fee = fee == null ? BigDecimal.ZERO : fee;
        this.paymentMethod = paymentMethod;
        this.counterparty = counterparty;
        this.timestamp = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getWalletId() {
        return walletId;
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
