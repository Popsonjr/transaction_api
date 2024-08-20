package com.stanbic.redbox.debit.service.dto.monnify.requests;

import lombok.Data;

@Data
public class BulkTransferSubRequest {
    private Float amount;
    private String reference;
    private String narration;
    private String destinationBankCode;
    private String destinationAccountNumber;
    private String currency;
}
