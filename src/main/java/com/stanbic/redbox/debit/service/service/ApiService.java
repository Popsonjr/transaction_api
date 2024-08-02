package com.stanbic.redbox.debit.service.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ApiService {
  public HttpResponse<String> postApiResponse   (String uri) {
    HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
//            .POST((HttpRequest.BodyPublisher) payload)
//            .header("Accept", "application/json")
//            .header("Content-Type", "application/json")
            .uri(URI.create(uri))
            .build();

    HttpClient httpClient = HttpClient.newHttpClient();
      try {
          return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException | InterruptedException e) {
          throw new RuntimeException(e);
      }

  }

}
