package com.stanbic.redbox.debit.service.service.monnify;


import com.stanbic.redbox.debit.service.dto.monnify.requests.wallet.CreateWalletRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.service.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WalletService {
    @Value("${monnify.api.base-url}")
    private String baseUrl;

    private final HttpClientUtil httpClient;

    public ResponseEntity<MonnifyResponse> handleCreateWallet(CreateWalletRequest walletRequest) {
        String url = baseUrl + "/api/v1/disbursements/wallet";
        return httpClient.withBasicAuth().post(url, walletRequest);
    }
}
