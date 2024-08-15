package com.stanbic.redbox.debit.service.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanbic.redbox.debit.service.dto.TransferRequest;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.model.monnify.AccessTokenResponse;
//import org.apache.http.HttpRequest;
import com.stanbic.redbox.debit.service.util.MonnifyUtils;
import com.stanbic.redbox.debit.service.util.TransactionReferenceGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class MonnifyService {
    @Value("${monnify.api.base-url}")
    private String baseUrl;

    private final WebClientService webClientService;
//    private final TransactionReferenceGenerator transactionReferenceGenerator;

    private final MonnifyUtils monnifyUtils;
    private String accessToken;
    private Long expiresIn;
    private Instant tokenGenerationTime;
    private final ObjectMapper objectMapper;


    @SneakyThrows
    public String handleGetAccessToken() {
        if (accessToken == null || isAccessTokenExpired())
            fetchAccessToken();

        return accessToken;
    }

    private void fetchAccessToken() throws IOException, InterruptedException {
        String url = baseUrl + "/api/v1/auth/login";

        HashMap<String, Object> payload = new HashMap<>();
        ResponseEntity<Object> response = webClientService.postRequest(url, payload, Object.class, monnifyUtils.getAuthKey());


        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new CustomRuntimeException(ResponseCodes.BAD_REQUEST, "Bad request");
        }
        AccessTokenResponse accessTokenResponse = objectMapper.convertValue(response.getBody(), AccessTokenResponse.class);
        accessToken = accessTokenResponse.getResponseBody().getAccessToken();
        tokenGenerationTime = Instant.now();
        expiresIn = accessTokenResponse.getResponseBody().getExpiresIn();
    }

    public Boolean isAccessTokenExpired() {
        return Duration.between(tokenGenerationTime, Instant.now()).getSeconds() >= expiresIn;
    }

    @SneakyThrows
    public Object handleInitiateTransfer(TransferRequest transferRequest) {
        transferRequest.setReference(TransactionReferenceGenerator.generateReference());
        String url = baseUrl + "/api/v2/disbursements/single";
        String token = handleGetAccessToken();
        String Authorization = "Bearer " + token;
        System.out.println(Authorization);

        return webClientService.postRequest(url, transferRequest, Object.class, Authorization);
    }

//    public TransactionDetails getTransactionDetails(String transactionReference) {
//        String url = baseUrl + "/api/v1/transactions/" + transactionReference;
//    }
}
