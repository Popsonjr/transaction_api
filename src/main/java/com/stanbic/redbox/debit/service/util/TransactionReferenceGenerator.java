package com.stanbic.redbox.debit.service.util;

import java.util.UUID;

public class TransactionReferenceGenerator {
    public static String generateReference() {
        return UUID.randomUUID().toString();
    }
}
