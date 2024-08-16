package com.stanbic.redbox.debit.service.controller;

import com.stanbic.redbox.debit.service.dto.requests.AuthorizeSingleTransferRequest;
import com.stanbic.redbox.debit.service.dto.requests.BulkTransferRequest;
import com.stanbic.redbox.debit.service.dto.requests.TransferRequest;
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

    @PostMapping("/initiate-bulk-transfer")
    public Object initiateBulkTransfer(@RequestBody BulkTransferRequest bulkTransferRequest) {
        return monnifyService.handleInitiateBulkTransfer(bulkTransferRequest);
    }

    @PostMapping("/authorize-single_transfer")
    public Object authorizeSingleTransfer(@RequestBody AuthorizeSingleTransferRequest transferRequest) {
        return monnifyService.handleAuthorizeSingleTransfers(transferRequest);
    }

//    @GetMapping("/transactions/{transactionTReference}")
//    public ResponseEntity<TransactionDetails>
//    getTransactionDetails(@PathVariable String transactionReference) {
//        TransactionDetails details = monnifyService.getTransactionDetails(transactionReference);
//        return ResponseEntity.ok(details);
//    }
}
