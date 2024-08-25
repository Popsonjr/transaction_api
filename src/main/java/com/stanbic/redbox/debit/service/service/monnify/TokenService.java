package com.stanbic.redbox.debit.service.service.monnify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanbic.redbox.debit.service.clients.TokenClientService;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.model.monnify.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${monnify.api.secret-key}")
    private String secretKey;
    @Value("${monnify.api.api-key}")
    private String apiKey;
    @Value("${monnify.api.base-url}")
    private String baseUrl;


    private String accessToken;
    private Long expiresIn;
    private Instant tokenGenerationTime;
    private final ObjectMapper objectMapper;

    private final TokenClientService tokenClientService;

    public String getAuthKey() {
        String authKey = apiKey + ":" + secretKey;
        return "Basic " + Base64.getEncoder().encodeToString(authKey.getBytes());
    }

    @SneakyThrows
    public String handleGetAccessToken() {
        if (accessToken == null || isAccessTokenExpired())
            fetchAccessToken();
        return accessToken;
    }

    public Boolean isAccessTokenExpired() {
        return Duration.between(tokenGenerationTime, Instant.now()).getSeconds() >= expiresIn;
    }

    public void fetchAccessToken() throws IOException, InterruptedException {
        String url = baseUrl + "/api/v1/auth/login";

        HashMap<String, Object> payload = new HashMap<>();
        ResponseEntity<Object> response = tokenClientService.postRequest(url, payload, Object.class, getAuthKey());


        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new CustomRuntimeException(ResponseCodes.BAD_REQUEST, "Bad request");
        }
        AccessTokenResponse accessTokenResponse = objectMapper.convertValue(response.getBody(), AccessTokenResponse.class);
        accessToken = accessTokenResponse.getResponseBody().getAccessToken();
        tokenGenerationTime = Instant.now();
        expiresIn = accessTokenResponse.getResponseBody().getExpiresIn();
    }

    public String getBearerToken() {
        String token = handleGetAccessToken();
        String Authorization = "Bearer " + token;
//        System.out.println(Authorization);
        return Authorization;
    }

}
