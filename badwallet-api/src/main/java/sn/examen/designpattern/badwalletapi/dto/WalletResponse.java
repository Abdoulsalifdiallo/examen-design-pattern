package sn.examen.designpattern.badwalletapi.dto;

import sn.examen.designpattern.badwalletapi.domain.Wallet;

import java.math.BigDecimal;
import java.time.Instant;

public class WalletResponse {

    private Long id;
    private String code;
    private String phoneNumber;
    private String email;
    private BigDecimal balance;
    private String currency;
    private Instant createdAt;

    public WalletResponse(Wallet wallet) {
        this.id = wallet.getId();
        this.code = wallet.getCode();
        this.phoneNumber = wallet.getPhoneNumber();
        this.email = wallet.getEmail();
        this.balance = wallet.getBalance();
        this.currency = wallet.getCurrency();
        this.createdAt = wallet.getCreatedAt();
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
}
