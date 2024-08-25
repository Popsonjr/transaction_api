package com.stanbic.redbox.debit.service.controller;

import com.stanbic.redbox.debit.service.dto.monnify.requests.AuthorizeTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.BulkTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.OtpRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.TransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.service.monnify.MonnifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monnify")
@RequiredArgsConstructor
public class MonnifyController {
    private final MonnifyService monnifyService;

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
    public ResponseEntity<MonnifyResponse> authorizeBulkTransfer(@RequestBody AuthorizeTransferRequest transferRequest) {
        return monnifyService.handleAuthorizeBulkTransfer(transferRequest);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<MonnifyResponse> sendOtp(@RequestBody OtpRequest otpRequest) {
        return monnifyService.handleSendOTP(otpRequest);
    }

    @GetMapping("/single-transfer-status")
    public ResponseEntity<MonnifyResponse> getSingleTransferStatus(@RequestParam String reference) {
        return monnifyService.handleGetSingleTransferStatus(reference);
    }

    @GetMapping("/bulk-transfer-status")
    public ResponseEntity<MonnifyResponse> getBulkTransferStatus(@RequestParam String batchReference) {
        return monnifyService.handleGetBulkTransferStatus(batchReference);
    }

    @GetMapping("/list-single-transfers")
    public ResponseEntity<MonnifyResponse> listAllSingleTransfers(@RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        return monnifyService.handleListAllSingleTransfers(pageSize, pageNo);
    }

    @GetMapping("/list-bulk-transfers")
    public ResponseEntity<MonnifyResponse> listAllBulkTransfers(@RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        return monnifyService.handleListAllBulkTransfers(pageSize, pageNo);
    }

    @GetMapping("/bulk-transfer-transactions")
    public ResponseEntity<MonnifyResponse> getBulkTransferTransactions(@RequestParam String batchReference, @RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        return monnifyService.handleGetBulkTransferTransactions(batchReference, pageSize, pageNo);
    }

    @GetMapping("/disbursements/wallet-balance")
    public ResponseEntity<MonnifyResponse> getWalletBalByAccNo (@RequestParam String accountNumber) {
        return monnifyService.handleGetWalletBalByAccNo(accountNumber);
    }

//    @GetMapping("/disbursements/search-transactions")
//    public ResponseEntity<MonnifyResponse> searchDisbursementsTransactions(@RequestParam String sourceAccountNumber, @RequestParam Integer pageSize, @RequestParam Integer pageNo, @RequestParam String startDate, @RequestParam String endDate, @RequestParam String amountFrom, @RequestParam String amountTo) {
//        return monnifyService.handleSearchDisbursementTransactions(sourceAccountNumber, pageSize, pageNo, startDate, endDate, amountFrom, amountTo);
//    }


//    @GetMapping("/transactions/{transactionTReference}")
//    public ResponseEntity<TransactionDetails>
//    getTransactionDetails(@PathVariable String transactionReference) {
//        TransactionDetails details = monnifyService.getTransactionDetails(transactionReference);
//        return ResponseEntity.ok(details);
//    }
}
