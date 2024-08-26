package com.stanbic.redbox.debit.service.dto.monnify.requests.wallet;

import lombok.Data;

@Data
public class CreateWalletRequest {
    private String walletReference;
    private String walletName;
    private String customerName;
    private BvnDetails bvnDetails;
    private String customerEmail;
}

