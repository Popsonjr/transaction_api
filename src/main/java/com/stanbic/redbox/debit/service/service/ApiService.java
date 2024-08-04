package com.stanbic.redbox.debit.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
//import java.net.http.HttpResponse;

@Service
public class ApiService {
    private final ObjectMapper objectMapper;

    public ApiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
//    public HttpResponse getApiResponse (String url) throws IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .GET()
//                .header("Accept", "application/json")
//                .uri(URI.create(url))
//                .build();
//
//        HttpClient httpClient = HttpClient.newHttpClient();
//        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//    }
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


  public String getResponse(String url) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity);
        }
  }

    public String postResponse(String url, Object requestBody) throws IOException {
      try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
          HttpPost request = new HttpPost(url);
          String requestBodyString = objectMapper.writeValueAsString(requestBody);
          StringEntity entity = new StringEntity(requestBodyString);
          request.setEntity(entity);
          request.setHeader("Content-Type", "application/json");
          HttpResponse httpResponse = httpClient.execute(request);
          HttpEntity responseEntity = httpResponse.getEntity();
          return EntityUtils.toString(responseEntity);
      }
    }
}
