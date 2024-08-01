package com.stanbic.redbox.debit.service.exceptions;

import com.stanbic.redbox.debit.service.dto.response.RedboxResponse;
import com.stanbic.redbox.debit.service.exceptions.custom.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<?> handleCustomRuntimeException (CustomRuntimeException ex) {
        return new ResponseEntity<>(
                new RedboxResponse(ex.getResponseEntity().getResponseCode(), ex.getResponseEntity().getResponseMessage(), ex.getResponseDetails()), ex.getResponseEntity().getHttpStatus()
        );
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleUncaughtExceptions (Exception ex) {
//
//    }
}
