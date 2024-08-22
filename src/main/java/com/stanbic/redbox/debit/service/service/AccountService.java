package com.stanbic.redbox.debit.service.service;

import com.stanbic.redbox.debit.service.model.Account;
import com.stanbic.redbox.debit.service.dto.response.RedboxResponse;
import com.stanbic.redbox.debit.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<RedboxResponse> createAccount(@RequestBody Account account) {
        System.out.println("saving account to DB");
        accountRepository.save(account);
        System.out.println("account saved to DB");
        return new ResponseEntity<>(new RedboxResponse("00", "Success", "account '" + account.getAccountNumber() + "' saved to DB"), HttpStatus.CREATED);
    }

    public ResponseEntity<RedboxResponse> updateAccount(Account account) {
        List<Account> accountCheck = accountRepository.findByAccountNumber(account.getAccountNumber());
        if (accountCheck.isEmpty())
            return new ResponseEntity<>(new RedboxResponse("99", "Failed", "Account not found"), HttpStatus.OK);
        Account acc = accountCheck.get(0);
        acc.setBalance(account.getBalance() != null ? account.getBalance() : acc.getBalance());
        acc.setAccountName(account.getAccountName() != null ? account.getAccountName() : acc.getAccountName());
        accountRepository.save(acc);
        return new ResponseEntity<>(new RedboxResponse("00", "Success", "account '" + account.getAccountNumber() + "' updated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<RedboxResponse> deleteAccount(String accountNumber) {
        List<Account> accountCheck = accountRepository.findByAccountNumber(accountNumber);
        if (accountCheck.isEmpty())
            return new ResponseEntity<>(new RedboxResponse("99", "Failed", "Account not found"), HttpStatus.OK);
        accountRepository.deleteById(accountCheck.get(0).getId());
        return new ResponseEntity<>(new RedboxResponse("00", "Success", "account '" + accountNumber + "' deleted successfully"), HttpStatus.OK);
    }
}
