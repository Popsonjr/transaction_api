package com.stanbic.redbox.debit.service.dto.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private String sourceAccountNumber;
    private BigDecimal amount;
    private String channelID;
    private String type;
    private String targetAccountNumber;
}
