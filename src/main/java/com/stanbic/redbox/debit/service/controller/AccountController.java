package com.stanbic.redbox.debit.service.controller;

import com.stanbic.redbox.debit.service.model.Account;
import com.stanbic.redbox.debit.service.repository.AccountRepository;
import com.stanbic.redbox.debit.service.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create-user")
    public ResponseEntity<?> create(@RequestBody Account account){
        System.out.println("/create-user received a request");
        return accountService.createAccount(account);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody Account account) {
        return accountService.updateAccount(account);
    }

    @DeleteMapping("/delete-user/{accountNumber}")
    public ResponseEntity<?> deleteUser(@PathVariable String accountNumber) {
        return accountService.deleteAccount(accountNumber);
    }
}
