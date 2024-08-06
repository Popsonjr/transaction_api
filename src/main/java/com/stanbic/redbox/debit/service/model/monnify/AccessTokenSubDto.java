package com.stanbic.redbox.debit.service.model.monnify;

import lombok.Data;

@Data
public class AccessTokenSubDto {
    private String accessToken;
    private Long expiresIn;
}
