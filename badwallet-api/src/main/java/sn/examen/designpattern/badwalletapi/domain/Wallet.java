package sn.examen.designpattern.badwalletapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Instant createdAt;

    @Version
    private Long version;

    protected Wallet() {
    }

    private Wallet(Builder builder) {
        this.code = builder.code;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.balance = builder.balance;
        this.currency = builder.currency;
        this.createdAt = Instant.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Builder : construit un Wallet en imposant les champs obligatoires et en
     * appliquant la regle de generation de code si aucun code n'est fourni.
     */
    public static final class Builder {
        private String code;
        private String phoneNumber;
        private String email;
        private BigDecimal balance = BigDecimal.ZERO;
        private String currency = "XOF";

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance == null ? BigDecimal.ZERO : balance;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency == null ? "XOF" : currency;
            return this;
        }

        public Wallet build() {
            if (phoneNumber == null || phoneNumber.isBlank()) {
                throw new IllegalStateException("Le numero de telephone est obligatoire");
            }
            if (code == null || code.isBlank()) {
                throw new IllegalStateException("Le code du portefeuille est obligatoire");
            }
            return new Wallet(this);
        }
    }
}
