package com.stanbic.redbox.debit.service.service.monnify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanbic.redbox.debit.service.dto.monnify.requests.AuthorizeTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.BulkTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.OtpRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.TransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.enums.TokenType;
import com.stanbic.redbox.debit.service.service.WebClientService;
import com.stanbic.redbox.debit.service.util.TransactionReferenceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.*;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service

public class MonnifyService {
    @Value("${monnify.api.base-url}")
    private String baseUrl;

    private String accessToken;
    private Long expiresIn;
    private Instant tokenGenerationTime;
    private final ObjectMapper objectMapper;

    private final WebClient.Builder webClientBuilder;
    private final WebClientService webClientService;

//    private final TokenService tokenService;


    @SneakyThrows
    public ResponseEntity<MonnifyResponse> handleInitiateTransfer(TransferRequest transferRequest) {
        transferRequest.setReference(TransactionReferenceGenerator.generateReference());
        String url = baseUrl + "/api/v2/disbursements/single";
        return webClientService.monnifyRequest(url, transferRequest, TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleInitiateBulkTransfer(BulkTransferRequest bulkTransferRequest) {
        bulkTransferRequest.setBatchReference(TransactionReferenceGenerator.generateReference());
        String url = baseUrl + "/api/v2/disbursements/batch";
        return  webClientService.monnifyRequest(url, bulkTransferRequest, TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleAuthorizeSingleTransfers(AuthorizeTransferRequest transferRequest) {
        String url = baseUrl + "/api/v2/disbursements/single/validate-otp";
        return webClientService.monnifyRequest(url, transferRequest,  TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleAuthorizeBulkTransfer(AuthorizeTransferRequest transferRequest) {
        String url = baseUrl + "/api/v2/disbursements/batch/validate-otp";
        return webClientService.monnifyRequest(url, transferRequest,  TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleSendOTP(OtpRequest otpRequest) {
        String url = baseUrl + "/api/v2/disbursements/single/resend-otp";
        return webClientService.monnifyRequest(url, otpRequest,  TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleGetSingleTransferStatus(String reference) {
        String fullURL = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/single/summary")
                .queryParam("reference", reference)
                .build()
                .toUriString();
        return webClientService.getRequest(fullURL, TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleGetBulkTransferStatus(String batchReference) {
        String fullURL = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/bulk/" + batchReference + "/transactions")
//                .queryParam("batchReference", batchReference)
                .build()
                .toUriString();
        return webClientService.getRequest(fullURL, TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleListAllSingleTransfers(Integer pageSize, Integer pageNo) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/single/transactions")
                .queryParam("pageSize", pageSize)
                .queryParam("pageNo", pageNo)
                .build()
                .toUriString();
        return webClientService.getRequest(url, TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleListAllBulkTransfers(Integer pageSize, Integer pageNo) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/bulk/transactions")
                .queryParam("pageSize", pageSize)
                .queryParam("pageNo", pageNo)
                .build()
                .toUriString();
        return webClientService.getRequest(url, TokenType.BEARER);
    }

    public ResponseEntity<MonnifyResponse> handleGetBulkTransferTransactions(String batchReference, Integer pageSize, Integer pageNo) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/bulk/" + batchReference + "/transactions")
                .queryParam("pageSize", pageSize)
                .queryParam("pageNo", pageNo)
                .build()
                .toUriString();
        return webClientService.getRequest(url, TokenType.BEARER);
    }

    @SneakyThrows
    public ResponseEntity<MonnifyResponse> handleSearchDisbursementTransactions(String sourceAccountNumber, Integer pageSize, Integer pageNo, String startDate, String endDate, String amountFrom, String amountTo) {

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        String startDate = zonedDateTime.format(formatter);

//        String endDate = zonedDateTime.format(formatter);

        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/search-transactions")
                .queryParam("sourceAccountNumber", sourceAccountNumber)
                .queryParam("pageSize", pageSize)
                .queryParam("pageNo", pageNo)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("amountFrom", amountFrom)
                .queryParam("amountTo", amountTo)
                .build()
                .toUriString();

        return webClientService.getRequest(url, TokenType.BEARER);
    }

//    public TransactionDetails getTransactionDetails(String transactionReference) {
//        String url = baseUrl + "/api/v1/transactions/" + transactionReference;
//    }
}
