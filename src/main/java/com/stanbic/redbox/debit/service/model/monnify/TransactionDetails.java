package com.stanbic.redbox.debit.service.model.monnify;

import lombok.Data;

@Data
public class TransactionDetails {
    private String transactionReference;
    private String paymentReference;
    private String amountPaid;
    private String paymentStatus;
}
