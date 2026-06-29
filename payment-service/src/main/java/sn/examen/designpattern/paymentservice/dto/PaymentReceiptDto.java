package sn.examen.designpattern.paymentservice.dto;

import sn.examen.designpattern.paymentservice.domain.ServiceName;

import java.math.BigDecimal;
import java.util.List;

public class PaymentReceiptDto {

    private String walletCode;
    private ServiceName serviceName;
    private BigDecimal amountCharged;
    private List<String> paidReferences;

    public PaymentReceiptDto(String walletCode, ServiceName serviceName, BigDecimal amountCharged,
                              List<String> paidReferences) {
        this.walletCode = walletCode;
        this.serviceName = serviceName;
        this.amountCharged = amountCharged;
        this.paidReferences = paidReferences;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public BigDecimal getAmountCharged() {
        return amountCharged;
    }

    public List<String> getPaidReferences() {
        return paidReferences;
    }
}
