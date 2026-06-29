package sn.examen.designpattern.badwalletapi.dto;

import java.math.BigDecimal;

public class BalanceResponse {

    private String phoneNumber;
    private BigDecimal balance;
    private String currency;

    public BalanceResponse(String phoneNumber, BigDecimal balance, String currency) {
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.currency = currency;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}
