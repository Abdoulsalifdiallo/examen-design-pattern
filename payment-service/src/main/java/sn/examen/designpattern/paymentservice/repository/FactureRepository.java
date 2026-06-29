package sn.examen.designpattern.paymentservice.repository;

import sn.examen.designpattern.paymentservice.domain.Facture;
import sn.examen.designpattern.paymentservice.domain.ServiceName;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    Optional<Facture> findByReference(String reference);

    List<Facture> findByWalletCodeAndPaidFalseAndMoisAndAnnee(String walletCode, int mois, int annee);

    List<Facture> findByWalletCodeAndServiceNameAndPaidFalseAndMoisAndAnnee(
            String walletCode, ServiceName serviceName, int mois, int annee);

    List<Facture> findByWalletCodeAndPaidFalseAndDateEcheanceBetween(
            String walletCode, LocalDate debut, LocalDate fin);

    boolean existsByWalletCode(String walletCode);
}
