package sn.examen.designpattern.badwalletapi.repository;

import sn.examen.designpattern.badwalletapi.domain.Wallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByPhoneNumber(String phoneNumber);

    Optional<Wallet> findByCode(String code);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCode(String code);
}
