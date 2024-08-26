package com.stanbic.redbox.debit.service.controller.monnify;

import com.stanbic.redbox.debit.service.dto.monnify.requests.disbursements.AuthorizeTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.disbursements.BulkTransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.disbursements.OtpRequest;
import com.stanbic.redbox.debit.service.dto.monnify.requests.disbursements.TransferRequest;
import com.stanbic.redbox.debit.service.dto.monnify.response.MonnifyResponse;
import com.stanbic.redbox.debit.service.service.monnify.DisbursementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monnify")
@RequiredArgsConstructor
public class DisbursementController {
    private final DisbursementService disbursementService;

    @PostMapping("/initiate-transfer")
    public Object initiateTransfer(@RequestBody TransferRequest transferRequest) {
        return disbursementService.handleInitiateTransfer(transferRequest);
    }

    @PostMapping("/initiate-bulk-transfer")
    public Object initiateBulkTransfer(@RequestBody BulkTransferRequest bulkTransferRequest) {
        return disbursementService.handleInitiateBulkTransfer(bulkTransferRequest);
    }

    @PostMapping("/authorize-single-transfer")
    public Object authorizeSingleTransfer(@RequestBody AuthorizeTransferRequest transferRequest) {
        return disbursementService.handleAuthorizeSingleTransfers(transferRequest);
    }

    @PostMapping("/authorize-bulk-transfer")
    public ResponseEntity<MonnifyResponse> authorizeBulkTransfer(@RequestBody AuthorizeTransferRequest transferRequest) {
        return disbursementService.handleAuthorizeBulkTransfer(transferRequest);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<MonnifyResponse> sendOtp(@RequestBody OtpRequest otpRequest) {
        return disbursementService.handleSendOTP(otpRequest);
    }

    @GetMapping("/single-transfer-status")
    public ResponseEntity<MonnifyResponse> getSingleTransferStatus(@RequestParam String reference) {
        return disbursementService.handleGetSingleTransferStatus(reference);
    }

    @GetMapping("/bulk-transfer-status")
    public ResponseEntity<MonnifyResponse> getBulkTransferStatus(@RequestParam String batchReference) {
        return disbursementService.handleGetBulkTransferStatus(batchReference);
    }

    @GetMapping("/list-single-transfers")
    public ResponseEntity<MonnifyResponse> listAllSingleTransfers(@RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        return disbursementService.handleListAllSingleTransfers(pageSize, pageNo);
    }

    @GetMapping("/list-bulk-transfers")
    public ResponseEntity<MonnifyResponse> listAllBulkTransfers(@RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        return disbursementService.handleListAllBulkTransfers(pageSize, pageNo);
    }

    @GetMapping("/bulk-transfer-transactions")
    public ResponseEntity<MonnifyResponse> getBulkTransferTransactions(@RequestParam String batchReference, @RequestParam Integer pageSize, @RequestParam Integer pageNo) {
        return disbursementService.handleGetBulkTransferTransactions(batchReference, pageSize, pageNo);
    }

    @GetMapping("/disbursements/wallet-balance")
    public ResponseEntity<MonnifyResponse> getWalletBalByAccNo (@RequestParam String accountNumber) {
        return disbursementService.handleGetWalletBalByAccNo(accountNumber);
    }

//    @GetMapping("/disbursements/search-transactions")
//    public ResponseEntity<MonnifyResponse> searchDisbursementsTransactions(@RequestParam String sourceAccountNumber, @RequestParam Integer pageSize, @RequestParam Integer pageNo, @RequestParam String startDate, @RequestParam String endDate, @RequestParam String amountFrom, @RequestParam String amountTo) {
//        return disbursementService.handleSearchDisbursementTransactions(sourceAccountNumber, pageSize, pageNo, startDate, endDate, amountFrom, amountTo);
//    }


//    @GetMapping("/transactions/{transactionTReference}")
//    public ResponseEntity<TransactionDetails>
//    getTransactionDetails(@PathVariable String transactionReference) {
//        TransactionDetails details = disbursementService.getTransactionDetails(transactionReference);
//        return ResponseEntity.ok(details);
//    }
}
