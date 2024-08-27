package com.stanbic.redbox.debit.service.service;

import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.service.monnify.TokenService;
import lombok.AllArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@Service
public class HttpClientUtil {
    private WebClient.Builder webClientBuilder;
    private final TokenService tokenService;

    public HttpClientUtil withBearerToken() {
        webClientBuilder = webClientBuilder.defaultHeader("Authorization", tokenService.getBearerToken());
        return this;
    }

    public HttpClientUtil withBasicAuth() {
        webClientBuilder = webClientBuilder.defaultHeader("Authorization", tokenService.getAuthKey());
        return this;
    }

    public ResponseEntity<MonnifyResponse> post(String url, Object requestBody) {
        WebClient webClient = webClientBuilder.build();
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

    public ResponseEntity<MonnifyResponse> get(String url, Object requestBody) {
        WebClient webClient = webClientBuilder.build();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        WebClient.RequestBodySpec request = webClient.method(HttpMethod.GET)
                .uri(uriBuilder.build().toUri());
        if (requestBody != null) request.body(BodyInserters.fromValue(requestBody));

        return request.accept(MediaType.APPLICATION_JSON)
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
