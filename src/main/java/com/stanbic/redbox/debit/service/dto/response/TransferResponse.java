package com.stanbic.redbox.debit.service.dto.response;

import lombok.Data;

@Data
public class TransferResponse<T> {
    private boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private T responseBody;
}

