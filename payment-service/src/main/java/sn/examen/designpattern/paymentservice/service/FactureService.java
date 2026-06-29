package sn.examen.designpattern.paymentservice.service;

import sn.examen.designpattern.paymentservice.billing.BillingRule;
import sn.examen.designpattern.paymentservice.billing.BillingRuleFactory;
import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;
import sn.examen.designpattern.paymentservice.dto.FactureDto;
import sn.examen.designpattern.paymentservice.dto.PaymentReceiptDto;
import sn.examen.designpattern.paymentservice.exception.FactureNotFoundException;
import sn.examen.designpattern.paymentservice.exception.InvalidPaymentRequestException;
import sn.examen.designpattern.paymentservice.repository.FactureRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final BillingRuleFactory billingRuleFactory;

    public FactureService(FactureRepository factureRepository, BillingRuleFactory billingRuleFactory) {
        this.factureRepository = factureRepository;
        this.billingRuleFactory = billingRuleFactory;
    }

    public List<FactureDto> getCurrentMonthFactures(String walletCode, ServiceName unite) {
        LocalDate now = LocalDate.now();
        List<Facture> factures = unite == null
                ? factureRepository.findByWalletCodeAndPaidFalseAndMoisAndAnnee(
                        walletCode, now.getMonthValue(), now.getYear())
                : factureRepository.findByWalletCodeAndServiceNameAndPaidFalseAndMoisAndAnnee(
                        walletCode, unite, now.getMonthValue(), now.getYear());
        return toDtos(factures);
    }

    public List<FactureDto> getFacturesByPeriod(String walletCode, LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) {
            throw new InvalidPaymentRequestException("La date de debut doit preceder la date de fin");
        }
        List<Facture> factures = factureRepository.findByWalletCodeAndPaidFalseAndDateEcheanceBetween(
                walletCode, debut, fin);
        return toDtos(factures);
    }

    public PaymentReceiptDto payCurrentMonth(String walletCode, ServiceName serviceName, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidPaymentRequestException("Le montant doit etre superieur a zero");
        }
        LocalDate now = LocalDate.now();
        List<Facture> unpaid = factureRepository
                .findByWalletCodeAndServiceNameAndPaidFalseAndMoisAndAnnee(
                        walletCode, serviceName, now.getMonthValue(), now.getYear())
                .stream()
                .sorted(Comparator.comparing(Facture::getDateEcheance))
                .toList();

        if (unpaid.isEmpty()) {
            throw new FactureNotFoundException(
                    "Aucune facture " + serviceName + " impayee ce mois pour " + walletCode);
        }

        BillingRule rule = billingRuleFactory.forService(serviceName);
        BigDecimal remaining = amount;
        List<String> paidReferences = new java.util.ArrayList<>();
        BigDecimal totalCharged = BigDecimal.ZERO;

        for (Facture facture : unpaid) {
            BigDecimal due = rule.computeAmountDue(facture);
            if (remaining.compareTo(due) < 0) {
                break;
            }
            facture.markPaid();
            factureRepository.save(facture);
            paidReferences.add(facture.getReference());
            totalCharged = totalCharged.add(due);
            remaining = remaining.subtract(due);
        }

        if (paidReferences.isEmpty()) {
            throw new InvalidPaymentRequestException(
                    "Montant insuffisant pour regler la facture la moins chere (" + unpaid.get(0).getReference() + ")");
        }

        return new PaymentReceiptDto(walletCode, serviceName, totalCharged, paidReferences);
    }

    public PaymentReceiptDto payByReferences(String walletCode, ServiceName serviceName, List<String> references) {
        if (references == null || references.isEmpty()) {
            throw new InvalidPaymentRequestException("La liste des references de factures est vide");
        }
        BillingRule rule = billingRuleFactory.forService(serviceName);
        BigDecimal totalCharged = BigDecimal.ZERO;
        List<String> paidReferences = new java.util.ArrayList<>();

        for (String reference : references) {
            Facture facture = factureRepository.findByReference(reference)
                    .orElseThrow(() -> new FactureNotFoundException("Facture introuvable : " + reference));
            if (!facture.getWalletCode().equals(walletCode) || facture.getServiceName() != serviceName) {
                throw new InvalidPaymentRequestException(
                        "La facture " + reference + " n'appartient pas a " + walletCode + "/" + serviceName);
            }
            if (facture.isPaid()) {
                continue;
            }
            BigDecimal due = rule.computeAmountDue(facture);
            facture.markPaid();
            factureRepository.save(facture);
            paidReferences.add(reference);
            totalCharged = totalCharged.add(due);
        }

        return new PaymentReceiptDto(walletCode, serviceName, totalCharged, paidReferences);
    }

    private List<FactureDto> toDtos(List<Facture> factures) {
        return factures.stream()
                .map(f -> new FactureDto(f, billingRuleFactory.forService(f.getServiceName()).computeAmountDue(f)))
                .collect(Collectors.toList());
    }
}
