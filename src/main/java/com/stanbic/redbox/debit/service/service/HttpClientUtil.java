package com.stanbic.redbox.debit.service.service;

import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.enums.TokenType;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import com.stanbic.redbox.debit.service.service.monnify.TokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

//@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class HttpClientUtil {
    private WebClient.Builder webClientBuildder;
    private final TokenService tokenService;

//    private HttpClientUtil(WebClient.Builder webClientBuildder) {
//        this.webClientBuildder = webClientBuildder;
//    }

    public HttpClientUtil withOAuth2() {
        webClientBuildder = webClientBuildder.defaultHeader("Authorization", tokenService.getBearerToken());
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


}
