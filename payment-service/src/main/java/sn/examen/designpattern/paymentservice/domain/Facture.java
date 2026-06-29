package sn.examen.designpattern.paymentservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private String walletCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceName serviceName;

    @Column(nullable = false)
    private BigDecimal montant;

    @Column(nullable = false)
    private int mois;

    @Column(nullable = false)
    private int annee;

    @Column(nullable = false)
    private LocalDate dateEcheance;

    @Column(nullable = false)
    private boolean paid;

    protected Facture() {
    }

    public Facture(String reference, String walletCode, ServiceName serviceName, BigDecimal montant,
                    int mois, int annee, LocalDate dateEcheance, boolean paid) {
        this.reference = reference;
        this.walletCode = walletCode;
        this.serviceName = serviceName;
        this.montant = montant;
        this.mois = mois;
        this.annee = annee;
        this.dateEcheance = dateEcheance;
        this.paid = paid;
    }

    public Long getId() {
        return id;
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

    public void markPaid() {
        this.paid = true;
    }
}
