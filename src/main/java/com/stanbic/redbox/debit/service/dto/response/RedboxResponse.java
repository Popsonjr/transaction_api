package com.stanbic.redbox.debit.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedboxResponse {
    private String responseCode;
    private String responseMsg;
    private Object responseDetails;
}
