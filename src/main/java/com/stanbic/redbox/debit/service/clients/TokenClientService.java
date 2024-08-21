package com.stanbic.redbox.debit.service.clients;

import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenClientService {
    private final WebClient webClient;
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
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}
