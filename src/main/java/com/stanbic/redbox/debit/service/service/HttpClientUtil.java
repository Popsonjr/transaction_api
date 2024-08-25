package com.stanbic.redbox.debit.service.service;

import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.service.monnify.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@Service
public class HttpClientUtil {
    private WebClient.Builder webClientBuildder;
    private final TokenService tokenService;

    public HttpClientUtil withBearerToken() {
        webClientBuildder = webClientBuildder.defaultHeader("Authorization", tokenService.getBearerToken());
        return this;
    }

    public HttpClientUtil withBasicAuth() {
        webClientBuildder = webClientBuildder.defaultHeader("Authorization", tokenService.getAuthKey());
        return this;
    }

    public ResponseEntity<MonnifyResponse> post(String url, Object requestBody) {
        WebClient webClient = webClientBuildder.build();
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
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
    }

    public ResponseEntity<MonnifyResponse> get(String url) {
        WebClient webClient = webClientBuildder.build();
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(error -> {
                                return Mono.error(new CustomRuntimeException(ResponseCodes.BAD_REQUEST, error));
                            });
                })
                .toEntity(new ParameterizedTypeReference<MonnifyResponse>() {
                })
                .block();
    }
}
