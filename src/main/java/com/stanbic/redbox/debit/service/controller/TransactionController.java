package com.stanbic.redbox.debit.service.controller;

import com.stanbic.redbox.debit.service.dto.requests.TransactionRequest;
import com.stanbic.redbox.debit.service.dto.response.RedboxResponse;
import com.stanbic.redbox.debit.service.dto.response.TransactionReceiptResponse;
import com.stanbic.redbox.debit.service.model.Transaction;
import com.stanbic.redbox.debit.service.service.ApiService;
import com.stanbic.redbox.debit.service.service.TransactionService;
import com.stanbic.redbox.debit.service.service.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
//    @Autowired
    private final TransactionService transactionService;
    private final ApiService apiService;

    @Autowired
    private WebClientService webClientService;

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

    @GetMapping("/randomUser")
    public Mono<String> getRandomUser(String url) {
        return webClientService.getApiResponse(url);
    }

    @SneakyThrows
    @GetMapping("/api")
    public ResponseEntity<?> api ()  {
        return ResponseEntity
                .ok()
                .body(apiService.getApiResponse("https://freetestapi.com/api/v1/actors/1"));
//        return transactionService.doHttpRequest("https://postman-echo.com/get");
    }

    @GetMapping("/webclient")
    public Mono<ResponseEntity<String>> getWithWebClient() {
        String url = "https://freetestapi.com/api/v1/actors/1";
        return webClientService.getApiResponse(url)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/webclient")
    public Mono<ResponseEntity<String>> postWithWebClient(@RequestBody Object requestBody) {
        String url = "https://jsonplaceholder.typicode.com/posts";
        return webClientService.postApiResponse(url, requestBody).map(response -> ResponseEntity.ok(response));
    }

//    @SneakyThrows
//    @GetMapping("/httpclient")
//    public String getWithHttp() {
//        String url = "https://freetestapi.com/api/v1/actors/1";
//        return apiService.getResponse(url);
//    }
//
//    @SneakyThrows
//    @PostMapping("/httpclient")
//    public String postWithHttp(@RequestBody Object requestBody) {
//        String url = "https://jsonplaceholder.typicode.com/posts";
//        return apiService.postResponse(url, requestBody);
//    }

}
