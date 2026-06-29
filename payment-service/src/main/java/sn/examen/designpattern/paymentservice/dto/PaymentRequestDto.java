package sn.examen.designpattern.paymentservice.dto;

import sn.examen.designpattern.paymentservice.domain.ServiceName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class PaymentRequestDto {

    @NotBlank
    private String walletCode;

    @NotNull
    private ServiceName serviceName;

    private BigDecimal amount;

    private List<String> factureReferences;

    public String getWalletCode() {
        return walletCode;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public void setServiceName(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<String> getFactureReferences() {
        return factureReferences;
    }

    public void setFactureReferences(List<String> factureReferences) {
        this.factureReferences = factureReferences;
    }
}
