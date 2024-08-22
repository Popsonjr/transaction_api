package com.stanbic.redbox.debit.service.repository;

import com.stanbic.redbox.debit.service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

List<Account> findByAccountNumber(String accountNumber);
}
