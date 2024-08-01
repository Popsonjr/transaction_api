package com.stanbic.redbox.debit.service.controller;

import com.stanbic.redbox.debit.service.dto.TransactionRequest;
import com.stanbic.redbox.debit.service.dto.response.RedboxResponse;
import com.stanbic.redbox.debit.service.dto.response.TransactionReceiptResponse;
import com.stanbic.redbox.debit.service.model.Transaction;
import com.stanbic.redbox.debit.service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<RedboxResponse> createTransaction(@Validated @RequestBody TransactionRequest transactionRequest) {
        return transactionService.transfer(transactionRequest);
    }

    @GetMapping("/date-range")
    public ResponseEntity<RedboxResponse> getTransactionForDateRange(@RequestParam("startTime") String startTime,
                                                                     @RequestParam("endTime") String endTime) {
        return transactionService.getTransactionsForDateRange(LocalDate.parse(startTime), LocalDate.parse(endTime));
    }

    @GetMapping("/{transactionId}/receipt")
    public ResponseEntity<RedboxResponse> generateReceipt(@PathVariable Long transactionId) {
        TransactionReceiptResponse receipt = transactionService.generateReceipt(transactionId);
        return new ResponseEntity<>(new RedboxResponse("00", "Success", receipt), HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<RedboxResponse> getTransactionHistory(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.getTransactionHistory(accountNumber);

        return new ResponseEntity<>(new RedboxResponse("00", "Success", transactions), HttpStatus.OK);
    }

    @PostMapping("/{accountNumber}/balance")
    public ResponseEntity<RedboxResponse> getBalance(@PathVariable String accountNumber) {
        return transactionService.handleGetBalance(accountNumber);
    }

    @SneakyThrows
    @GetMapping("/api")
    public HttpResponse api ()  {
        return transactionService.doHttpRequest("https://postman-echo.com/get");
    }
}
