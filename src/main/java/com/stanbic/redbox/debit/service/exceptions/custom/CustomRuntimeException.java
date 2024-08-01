package com.stanbic.redbox.debit.service.exceptions.custom;

import com.stanbic.redbox.debit.service.enums.ResponseCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomRuntimeException extends RuntimeException {
    private ResponseCodes responseEntity;
    private Object responseDetails;
}
