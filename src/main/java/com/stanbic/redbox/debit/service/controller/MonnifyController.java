package com.stanbic.redbox.debit.service.controller;

import com.stanbic.redbox.debit.service.dto.monnify.requests.AuthorizeTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.BulkTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.OtpRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.TransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.TransferResponse;
import com.stanbic.redbox.debit.service.service.monnify.MonnifyService;
import com.stanbic.redbox.debit.service.service.monnify.TokenService;
import com.stanbic.redbox.debit.service.util.MonnifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monnify")
//@AllArgsConstructor
@RequiredArgsConstructor
public class MonnifyController {
    private final MonnifyService monnifyService;
    private final TokenService tokenService;

    @PostMapping("/access")
    public String getAccessToken() {
        return tokenService.getAuthKey();
    }

    @PostMapping("/initiate-transfer")
    public Object initiateTransfer(@RequestBody TransferRequest transferRequest) {
        return monnifyService.handleInitiateTransfer(transferRequest);
    }

    @PostMapping("/initiate-bulk-transfer")
    public Object initiateBulkTransfer(@RequestBody BulkTransferRequest bulkTransferRequest) {
        return monnifyService.handleInitiateBulkTransfer(bulkTransferRequest);
    }

    @PostMapping("/authorize-single-transfer")
    public Object authorizeSingleTransfer(@RequestBody AuthorizeTransferRequest transferRequest) {
        return monnifyService.handleAuthorizeSingleTransfers(transferRequest);
    }

    @PostMapping("/authorize-bulk-transfer")
    public ResponseEntity<TransferResponse> authorizeBulkTransfer(@RequestBody AuthorizeTransferRequest transferRequest) {
        return monnifyService.handleAuthorizeBulkTransfer(transferRequest);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<TransferResponse> sendOtp(@RequestBody OtpRequest otpRequest) {
        return monnifyService.handleSendOTP(otpRequest);
    }

    @GetMapping("/single-transfer-status")
    public ResponseEntity<TransferResponse> getSingleTransferStatus(@RequestParam String reference) {
        return monnifyService.handleGetSingleTransferStatus(reference);
    }

//    @GetMapping("/transactions/{transactionTReference}")
//    public ResponseEntity<TransactionDetails>
//    getTransactionDetails(@PathVariable String transactionReference) {
//        TransactionDetails details = monnifyService.getTransactionDetails(transactionReference);
//        return ResponseEntity.ok(details);
//    }
}
