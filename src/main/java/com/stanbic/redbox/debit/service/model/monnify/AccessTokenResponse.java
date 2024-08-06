package com.stanbic.redbox.debit.service.model.monnify;

import lombok.Data;

@Data
public class AccessTokenResponse {
    private Boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private AccessTokenSubDto responseBody;
}
