package com.stanbic.redbox.debit.service.controller.monnify;

import com.stanbic.redbox.debit.service.dto.monnify.requests.wallet.CreateWalletRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.service.monnify.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create-wallet")
    public ResponseEntity<MonnifyResponse> createWallet(@RequestBody CreateWalletRequest walletRequest) {
        return walletService.handleCreateWallet(walletRequest);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<MonnifyResponse> GetWalletBalByWalletReference(@RequestParam String walletReference, @PathVariable String accountNumber) {
        return walletService.handleGetWalletBalByWalletReference(walletReference, accountNumber);
    }

    @GetMapping
    public ResponseEntity<MonnifyResponse> GetWallets(@RequestParam Integer pageSize, @RequestParam Integer pageNo, @RequestParam String customerEmail) {
        return walletService.handleGetWallets(pageSize, pageNo, customerEmail);
    }
}
