package com.stanbic.redbox.debit.service.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyFormatter {
    public static String format(BigDecimal number) {
        Currency naira = Currency.getInstance("NGN");
        Locale nigeria = new Locale("en", "NG");

        NumberFormat nairaFormatter = NumberFormat.getCurrencyInstance(nigeria);
        nairaFormatter.setCurrency(naira);
        return nairaFormatter.format(number);
    }
}
