package com.stanbic.redbox.debit.service.dto.monnify.response;

import lombok.Data;

@Data
public class MonnifyResponse<T> {
    private boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private T responseBody;
}

