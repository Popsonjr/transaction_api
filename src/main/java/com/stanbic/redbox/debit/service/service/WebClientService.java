package com.stanbic.redbox.debit.service.service;

import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.enums.TokenType;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.service.monnify.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class WebClientService {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private WebClient webClient;

    //    private String bearerToken;
    private final TokenService tokenService;

    public <T> ResponseEntity<MonnifyResponse> monnifyRequest(String url, Object requestBody, TokenType token) {
        if (token == TokenType.BEARER)
            return sendMonnifyRequest(url, requestBody, tokenService.getBearerToken());
        if (token == TokenType.AUTH)
            return sendMonnifyRequest(url, requestBody, tokenService.getAuthKey());

        return sendMonnifyRequest(url, requestBody, null);
    }


    public <T> ResponseEntity<MonnifyResponse> sendMonnifyRequest(String url, Object requestBody, String token) {
        try {
            return webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .header("Authorization", token)
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        return response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    return Mono.error(new CustomRuntimeException(ResponseCodes.BAD_REQUEST, errorBody));
                                });
                    })
                    .toEntity(new ParameterizedTypeReference<MonnifyResponse>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
//            return new ResponseEntity<>(new RedboxResponse("99", "Bad Request", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    public <T> ResponseEntity<MonnifyResponse> getRequest(String url, TokenType token) {
        try {
            WebClient.RequestHeadersUriSpec<?> requestSpec = (WebClient.RequestHeadersUriSpec<?>) webClientBuilder.build()
                    .get()
                    .uri(url);
            if (token == TokenType.BEARER) requestSpec.header("Authorization", tokenService.getBearerToken());
            if (token == TokenType.AUTH) requestSpec.header("Authorization", tokenService.getAuthKey());

            return requestSpec.accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        return response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    return Mono.error(new CustomRuntimeException(ResponseCodes.BAD_REQUEST, error));
                                });
                    })
                    .toEntity(new ParameterizedTypeReference<MonnifyResponse>() {
                    })
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error " + e.getMessage(), e);
        }
    }

    public <T> ResponseEntity<T> postRequest(String url, Object requestBody, Class<T> responseType, String authKey) {
        try {
            return webClient.post()
                    .uri(url)
                    .body(Mono.just(requestBody), Object.class)
                    .header("Authorization", authKey)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, res -> {
                        return res.bodyToMono(String.class)
                                .flatMap(error -> {
                                    return Mono.error(new CustomRuntimeException(ResponseCodes.BAD_REQUEST, error));
                                });
                    })
//                    .bodyToMono(responseType)
//                    .doOnNext(response-> System.out.println("Response: " + response))
                    .toEntity(responseType)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
//            return new ResponseEntity<>(new RedboxResponse("99", "Bad Request", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    public Mono<String> getApiResponse(String url) {
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> postApiResponse(String url, Object requestBody) {
        return webClientBuilder.build()
                .post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

}
