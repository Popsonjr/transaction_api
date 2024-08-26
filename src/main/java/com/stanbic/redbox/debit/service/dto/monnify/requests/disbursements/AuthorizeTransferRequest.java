package com.stanbic.redbox.debit.service.dto.monnify.requests.disbursements;

import lombok.Data;

@Data
public class AuthorizeTransferRequest {
    private String reference;
    private String authorizationCode;
}
