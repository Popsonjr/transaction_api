package com.stanbic.redbox.debit.service.dto.monnify.requests;

import lombok.Data;

@Data
public class AuthorizeTransferRequest {
    private String reference;
    private String authorizationCode;
}
