package sn.examen.designpattern.badwalletapi.repository;

import sn.examen.designpattern.badwalletapi.domain.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletIdOrderByTimestampDesc(Long walletId);
}
