package com.stanbic.redbox.debit.service.service.monnify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanbic.redbox.debit.service.dto.monnify.requests.AuthorizeTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.BulkTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.OtpRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.TransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.TransferResponse;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.enums.TokenType;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.model.monnify.AccessTokenResponse;
//import org.apache.http.HttpRequest;
import com.stanbic.redbox.debit.service.service.WebClientService;
import com.stanbic.redbox.debit.service.util.MonnifyUtils;
import com.stanbic.redbox.debit.service.util.TransactionReferenceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<TransferResponse> handleInitiateTransfer(TransferRequest transferRequest) {
        transferRequest.setReference(TransactionReferenceGenerator.generateReference());
        String url = baseUrl + "/api/v2/disbursements/single";
        return webClientService.monnifyRequest(url, transferRequest, TokenType.BEARER);
    }

    public ResponseEntity<TransferResponse> handleInitiateBulkTransfer(BulkTransferRequest bulkTransferRequest) {
        bulkTransferRequest.setBatchReference(TransactionReferenceGenerator.generateReference());
        String url = baseUrl + "/api/v2/disbursements/batch";
        return  webClientService.monnifyRequest(url, bulkTransferRequest, TokenType.BEARER);
    }

    public ResponseEntity<TransferResponse> handleAuthorizeSingleTransfers(AuthorizeTransferRequest transferRequest) {
        String url = baseUrl + "/api/v2/disbursements/single/validate-otp";
        return webClientService.monnifyRequest(url, transferRequest,  TokenType.BEARER);
    }

    public ResponseEntity<TransferResponse> handleAuthorizeBulkTransfer(AuthorizeTransferRequest transferRequest) {
        String url = baseUrl + "/api/v2/disbursements/batch/validate-otp";
        return webClientService.monnifyRequest(url, transferRequest,  TokenType.BEARER);
    }

    public ResponseEntity<TransferResponse> handleSendOTP(OtpRequest otpRequest) {
        String url = baseUrl + "/api/v2/disbursements/single/resend-otp";
        return webClientService.monnifyRequest(url, otpRequest,  TokenType.BEARER);
    }

    public ResponseEntity<TransferResponse> handleGetSingleTransferStatus(String reference) {
        String fullURL = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/single/summary")
                .queryParam("reference", reference)
                .build()
                .toUriString();
        return webClientService.getRequest(fullURL, TokenType.BEARER);
    }

    public ResponseEntity<TransferResponse> handleListAllSingleTransfers(Integer pageSize, Integer pageNo) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/single/transactions")
                .queryParam("pageSize", pageSize)
                .queryParam("pageNo", pageNo)
                .build()
                .toUriString();
        return webClientService.getRequest(url, TokenType.BEARER);
    }

    public ResponseEntity<TransferResponse> handleListAllBulkTransfers(Integer pageSize, Integer pageNo) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/bulk/transactions")
                .queryParam("pageSize", pageSize)
                .queryParam("pageNo", pageNo)
                .build()
                .toUriString();
        return webClientService.getRequest(url, TokenType.BEARER);
    }

//    public TransactionDetails getTransactionDetails(String transactionReference) {
//        String url = baseUrl + "/api/v1/transactions/" + transactionReference;
//    }
}
