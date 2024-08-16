package com.stanbic.redbox.debit.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkTransferRequest {
    private String title;
    private String batchReference;
    private String narration;
    private String sourceAccountNumber;
    private String onValidationFailure;
    private Integer notificationInterval;
    private List<BulkTransferSubRequest> transactionList;
}
