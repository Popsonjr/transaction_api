package com.stanbic.redbox.debit.service.service;

import com.stanbic.redbox.debit.service.dto.TransactionRequest;
import com.stanbic.redbox.debit.service.dto.response.TransactionReceiptResponse;
import com.stanbic.redbox.debit.service.dto.response.TransactionResponse;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.enums.TransactionType;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.model.Account;
import com.stanbic.redbox.debit.service.model.CustomerDetails;
import com.stanbic.redbox.debit.service.model.Transaction;
import com.stanbic.redbox.debit.service.dto.response.RedboxResponse;
import com.stanbic.redbox.debit.service.repository.AccountRepository;
import com.stanbic.redbox.debit.service.repository.TransactionRepository;
import com.stanbic.redbox.debit.service.util.TransactionReferenceGenerator;
import com.stanbic.redbox.debit.service.util.CurrencyFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;
    private final WebClientService webClientService;

    public ResponseEntity<RedboxResponse> transfer(TransactionRequest transactionRequest) {

        if (!AccountExists(transactionRequest.getSourceAccountNumber())) {
            throw new CustomRuntimeException(ResponseCodes.INVALID_ACCOUNT, "Invalid account. Check details and try again");
        }
        if (!AccountExists(transactionRequest.getTargetAccountNumber())) {
            throw new CustomRuntimeException(ResponseCodes.INVALID_ACCOUNT, "Invalid account. Check details and try again");
        }

        Account senderAccount = getAccountByAccountNumber(transactionRequest.getSourceAccountNumber());
        Account receiverAccount = getAccountByAccountNumber(transactionRequest.getTargetAccountNumber());

        if (senderAccount.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            throw new CustomRuntimeException(ResponseCodes.LOW_FUNDS, "Transfer amount is greater than balance");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionRequest.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionRequest.getAmount()));
        accountRepository.save(receiverAccount);
        accountRepository.save(senderAccount);

        String transactionReference = TransactionReferenceGenerator.generateReference();

        saveTransactions(transactionRequest, transactionReference, TransactionType.DEBIT);
        saveTransactions(transactionRequest, transactionReference, TransactionType.CREDIT);

        return new ResponseEntity<>(new RedboxResponse("00", "Success", "Transfer Completed"), HttpStatus.OK);
    }

    public ResponseEntity<RedboxResponse> getTransactionsForDateRange(LocalDate startTime, LocalDate endTime) {

        if (endTime == null || startTime == null || !startTime.isBefore(endTime))
            return new ResponseEntity<>(new RedboxResponse("99", "Failed", "Invalid date range"), HttpStatus.EXPECTATION_FAILED);

        List<Transaction> transactions = transactionRepository.findByTimestampRange(startTime, endTime);

        return new ResponseEntity<>(new RedboxResponse("00", "Success", transactions), HttpStatus.OK);

    }

    public TransactionReceiptResponse generateReceipt(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return TransactionReceiptResponse.builder()
                .receiptNumber(UUID.randomUUID().toString())
                .sourceAccountNumber(transaction.getSourceAccountNumber())
                .targetAccountNumber(transaction.getTargetAccountNumber())
                .transactionReference(transaction.getTransactionReference())
                .channelID(transaction.getChannelID())
                .amount(transaction.getAmount()).type(transaction.getType())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    private TransactionResponse generateTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .sourceAccountNumber(transaction.getSourceAccountNumber())
                .targetAccountNumber(transaction.getTargetAccountNumber())
                .amount(transaction.getAmount())
                .transactionReference(transaction.getTransactionReference())
                .channelID(transaction.getChannelID())
                .build();
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    public ResponseEntity<RedboxResponse> handleGetBalance(String accountNumber) {
        if (!AccountExists(accountNumber))
            return new ResponseEntity<>(new RedboxResponse("99", "Failed", "Account number does not exist"), HttpStatus.OK);
        Account account = getAccountByAccountNumber(accountNumber);
        if (account == null)
            return new ResponseEntity<>(new RedboxResponse("99", "Failed", "Account not found"), HttpStatus.OK);

        String responseDetails = "Account number: " + account.getAccountNumber() + " . Balance: " + CurrencyFormatter.format(account.getBalance()) + ".";
        return new ResponseEntity<>(new RedboxResponse("00", "Success", responseDetails), HttpStatus.OK);
    }

    public Boolean AccountExists(String accountNumber) {
        List<Account> accountCheck = accountRepository.findByAccountNumber(accountNumber);
        return !accountCheck.isEmpty();
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        List<Account> accountCheck = accountRepository.findByAccountNumber(accountNumber);
        return accountCheck.isEmpty() ? null : accountCheck.get(0);
    }

    public void saveTransactions(TransactionRequest transactionRequest, String txnRef, TransactionType transactionType) {
        transactionRepository
                .save(
                        Transaction.builder()
                                .sourceAccountNumber(transactionRequest.getSourceAccountNumber())
                                .targetAccountNumber(transactionRequest.getTargetAccountNumber())
                                .type(transactionType.name())
                                .transactionReference(txnRef)
                                .amount(transactionRequest.getAmount())
                                .channelID(transactionRequest.getChannelID())
                                .build()
                );
    }

    public HttpResponse doHttpRequest(String uri) throws URISyntaxException {
//        HttpRequest.newBuilder(request, (name, value) -> !name.equalsIgnoreCase("test"));
//        HttpRequest.newBuilder(new URI("https://postman-echo.com/get"));
        HttpRequest getRequest = HttpRequest.newBuilder().uri(new URI(uri))
//                .version(HttpClient.Version.HTTP_2)
                .headers("name", "Shade", "age", "29")
                .timeout(Duration.of(30, SECONDS))
                .GET()
                .build();

//        HttpRequest postRequest = HttpRequest.newBuilder()
//                .uri(new URI("https://postman-echo.com/post"))
//                .POST(HttpRequest.BodyPublishers.noBody())
//                .POST(HttpRequest.BodyPublishers.ofString("Body for test"))
////                .POST(HttpRequest.BodyPublishers.ofInputStream(
////                        () -> new ByteArrayInputStream(sampleData)
////                ))
//                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            return HttpClient.newBuilder()
                    .build()
                    .send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<ResponseEntity<?>> processCustomerDetails() {
        String randomUserApi = "https://randomuser.me/api/";
        return webClientService.getApiResponse(randomUserApi).map(response -> ResponseEntity.ok(response));
    }

    private String makeDecisionOnCustomer(CustomerDetails customerDetails) {
        if (customerDetails.getAge() < 18)
            return "Customer is not of age";
        else
            return "Customer is of age";
    }
}
