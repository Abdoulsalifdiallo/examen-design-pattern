package sn.examen.designpattern.badwalletapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PayFacturesRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String serviceName;

    @NotEmpty
    private List<String> factureReferences;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getFactureReferences() {
        return factureReferences;
    }

    public void setFactureReferences(List<String> factureReferences) {
        this.factureReferences = factureReferences;
    }
}
