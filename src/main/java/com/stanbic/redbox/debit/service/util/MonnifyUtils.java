package com.stanbic.redbox.debit.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class MonnifyUtils {
    @Value("${monnify.api.secret-key}")
    private String secretKey;
    @Value("${monnify.api.api-key}")
    private String apiKey;

    public String getAuthKey() {
        String authKey = apiKey + ":" + secretKey;
        return "Basic " + Base64.getEncoder().encodeToString(authKey.getBytes());
    }
}
