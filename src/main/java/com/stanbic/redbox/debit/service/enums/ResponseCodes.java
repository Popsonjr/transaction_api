package com.stanbic.redbox.debit.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCodes {
    INVALID_ACCOUNT("99", "Account does not exist", HttpStatus.BAD_REQUEST),
    LOW_FUNDS("99", "Insufficient funds", HttpStatus.BAD_REQUEST);

    private final String responseCode;
    private final String responseMessage;
    private final HttpStatus httpStatus;

    ResponseCodes(String responseCode, String responseMessage, HttpStatus httpStatus) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
    }

}
