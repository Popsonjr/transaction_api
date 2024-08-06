package com.stanbic.redbox.debit.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

@Service
public class ApiService {
    private final ObjectMapper objectMapper;

    public ApiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    public ResponseEntity<?> getApiResponse (String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(URI.create(url))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() + "\n\n");
        System.out.println(response.body());
        return ResponseEntity
                .ok()
                .body(response.body());
    }


//  public HttpResponse<String> postApiResponse   (String uri) {
//    HttpRequest httpRequest = HttpRequest.newBuilder()
//            .GET()
////            .POST((HttpRequest.BodyPublisher) payload)
//            .header("Accept", "application/json")
//            .header("Content-Type", "application/json")
//            .uri(URI.create(uri))
//            .build();
//
//    HttpClient httpClient = HttpClient.newHttpClient();
//      try {
//          return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//      } catch (IOException | InterruptedException e) {
//          throw new RuntimeException(e);
//      }
//
//  }


//  public String getResponse(String url) throws IOException {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
//            HttpGet request = new HttpGet(url);
//            HttpResponse httpResponse = httpClient.execute(request);
//            HttpEntity entity = httpResponse.getEntity();
//            return EntityUtils.toString(entity);
//        }
//  }

//    public String postResponse(String url, Object requestBody) throws IOException {
//      try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//          HttpPost request = new HttpPost(url);
//          String requestBodyString = objectMapper.writeValueAsString(requestBody);
//          StringEntity entity = new StringEntity(requestBodyString);
//          request.setEntity(entity);
//          request.setHeader("Content-Type", "application/json");
//          HttpResponse httpResponse = httpClient.execute(request);
//          HttpEntity responseEntity = httpResponse.getEntity();
//          return EntityUtils.toString(responseEntity);
//      }
//    }
}
