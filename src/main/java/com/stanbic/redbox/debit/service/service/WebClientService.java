package com.stanbic.redbox.debit.service.service;

import com.stanbic.redbox.debit.service.dto.response.RedboxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private WebClient webClient;

    public <T> T postRequest(String url, Object requestBody, Class <T> responseType, String authKey) {
        try {
            T returnValue = webClient.post()
                    .uri(url)
                    .body(Mono.just(requestBody), Object.class)
                    .header("Authorization", authKey)
                    .retrieve()
                    .bodyToMono(responseType)
//                    .doOnNext(response-> System.out.println("Response: " + response))
                    .block();

            return returnValue;
        } catch(WebClientResponseException e) {
//            System.out.println("Request failed with status code: " + e.getStatusCode());
//            System.out.println("Response body: " + e.getResponseBodyAsString());
//            System.out.println(e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
//            return new ResponseEntity<>(new RedboxResponse("99", "Bad Request", e.getMessage()), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
//            System.out.println(e.getMessage());
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
