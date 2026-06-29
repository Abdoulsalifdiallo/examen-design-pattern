package sn.examen.designpattern.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;

public class InitFacturesRequest {

    @NotBlank
    private String walletCode;

    public String getWalletCode() {
        return walletCode;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }
}
