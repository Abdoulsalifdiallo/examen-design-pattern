package sn.examen.designpattern.badwalletapi.proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Vue locale d'une facture renvoyee par payment-service, telle qu'exposee
 * par le Proxy de badwallet-api.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FactureView {

    private String reference;
    private String walletCode;
    private String serviceName;
    private BigDecimal montant;
    private BigDecimal montantDu;
    private int mois;
    private int annee;
    private LocalDate dateEcheance;
    private boolean paid;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getMontantDu() {
        return montantDu;
    }

    public void setMontantDu(BigDecimal montantDu) {
        this.montantDu = montantDu;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
