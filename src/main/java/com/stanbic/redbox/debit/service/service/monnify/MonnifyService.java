package com.stanbic.redbox.debit.service.service.monnify;

import com.stanbic.redbox.debit.service.dto.monnify.requests.AuthorizeTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.BulkTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.OtpRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.TransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.enums.TokenType;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.service.HttpClientUtil;
import com.stanbic.redbox.debit.service.service.WebClientService;
import com.stanbic.redbox.debit.service.util.TransactionReferenceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class MonnifyService {
    @Value("${monnify.api.base-url}")
    private String baseUrl;

    private final WebClientService webClientService;
    private final HttpClientUtil httpClientUtil;

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
        return httpClientUtil.withOAuth2().post(url, otpRequest);
//        return webClientService.monnifyRequest(url, otpRequest,  TokenType.BEARER);
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


    public ResponseEntity<MonnifyResponse> handleGetWalletBalByAccNo(String accountNumber) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/v2/disbursements/wallet-balance")
                .queryParam("accountNumber", accountNumber)
                .build()
                .toUriString();
        return webClientService.getRequest(url, TokenType.BEARER);
    }

//    @SneakyThrows
//    public ResponseEntity<MonnifyResponse> handleSearchDisbursementTransactions(String sourceAccountNumber, Integer pageSize, Integer pageNo, String startDate, String endDate, String amountFrom, String amountTo) {
//        System.out.println("here");
//
//        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        System.out.println("here1");
//        try {
//            System.out.println("here2");
//            // Parse the input string into a Date object
//            Date date = inputFormat.parse(startDate);
//            Date date1 = inputFormat.parse(endDate);
//            System.out.println("here3");
//            // Format the Date object back into a string
//            String formattedDate = outputFormat.format(date);
//            String formattedDate1 = outputFormat.format(date1);
//            System.out.println("here4");
//            // Print the formatted date string
//            System.out.println(formattedDate);
//
//
//        String url = UriComponentsBuilder.fromUriString(baseUrl)
//                .path("/api/v2/disbursements/search-transactions")
//                .queryParam("sourceAccountNumber", sourceAccountNumber)
//                .queryParam("pageSize", pageSize)
//                .queryParam("pageNo", pageNo)
//                .queryParam("startDate", formattedDate)
//                .queryParam("endDate", formattedDate1)
//                .queryParam("amountFrom", amountFrom)
//                .queryParam("amountTo", amountTo)
//                .build()
//                .toUriString();
//            System.out.println("here5");
//        return webClientService.getRequest(url, TokenType.BEARER);
//        } catch (ParseException e) {
//            throw new CustomRuntimeException(ResponseCodes.BAD_REQUEST, e);
////            System.err.println("Error parsing date: " + e.getMessage());
//        }
//    }

}
