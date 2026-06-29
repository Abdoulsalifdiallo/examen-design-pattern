package sn.examen.designpattern.paymentservice.dto;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FactureDto {

    private String reference;
    private String walletCode;
    private ServiceName serviceName;
    private BigDecimal montant;
    private BigDecimal montantDu;
    private int mois;
    private int annee;
    private LocalDate dateEcheance;
    private boolean paid;

    public FactureDto(Facture facture, BigDecimal montantDu) {
        this.reference = facture.getReference();
        this.walletCode = facture.getWalletCode();
        this.serviceName = facture.getServiceName();
        this.montant = facture.getMontant();
        this.montantDu = montantDu;
        this.mois = facture.getMois();
        this.annee = facture.getAnnee();
        this.dateEcheance = facture.getDateEcheance();
        this.paid = facture.isPaid();
    }

    public String getReference() {
        return reference;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public BigDecimal getMontantDu() {
        return montantDu;
    }

    public int getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public boolean isPaid() {
        return paid;
    }
}
