package com.stanbic.redbox.debit.service.controller;

import com.stanbic.redbox.debit.service.dto.TransferRequest;
import com.stanbic.redbox.debit.service.service.MonnifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monnify")
//@AllArgsConstructor
@RequiredArgsConstructor
public class MonnifyController {
    private final MonnifyService monnifyService;

    @PostMapping("/access")
    public String getAccessToken() {
        return monnifyService.handleGetAccessToken();
    }

    @PostMapping("/initiate-transfer")
    public Object initiateTransfer(@RequestBody TransferRequest transferRequest) {
        return monnifyService.handleInitiateTransfer(transferRequest);
    }

//    @GetMapping("/transactions/{transactionTReference}")
//    public ResponseEntity<TransactionDetails>
//    getTransactionDetails(@PathVariable String transactionReference) {
//        TransactionDetails details = monnifyService.getTransactionDetails(transactionReference);
//        return ResponseEntity.ok(details);
//    }
}
