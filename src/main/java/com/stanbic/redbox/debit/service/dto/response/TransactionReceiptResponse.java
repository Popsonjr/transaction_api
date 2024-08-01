package com.stanbic.redbox.debit.service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class TransactionReceiptResponse {
    private String receiptNumber;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String transactionReference;
    private BigDecimal amount;
    private String channelID;
    private String type;
    private LocalDate timestamp;
}
