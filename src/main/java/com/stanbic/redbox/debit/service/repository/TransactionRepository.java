package com.stanbic.redbox.debit.service.repository;

import com.stanbic.redbox.debit.service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transaction t WHERE t.source_account_number = :accountNumber OR t.target_account_number = :accountNumber", nativeQuery = true)
    List<Transaction> findByAccountNumber(String accountNumber);

    @Query(value = "SELECT * FROM transaction t WHERE t.timestamp BETWEEN :startTime and :endTime", nativeQuery = true)
    List<Transaction> findByTimestampRange(LocalDate startTime, LocalDate endTime);
}
