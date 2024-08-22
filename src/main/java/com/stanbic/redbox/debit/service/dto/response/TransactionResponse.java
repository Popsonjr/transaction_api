package com.stanbic.redbox.debit.service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String transactionReference;
    private BigDecimal amount;
    private String channelID;
    private String type;
    private LocalDate timestamp;
}
