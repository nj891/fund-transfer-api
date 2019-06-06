package com.finance.fund.transfer.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.finance.fund.transfer.utils.Validator;

/**
 * @author Nilesh
 */
@ToString
@EqualsAndHashCode
public class BaseCurrency implements Currency {
    private static final ConcurrentMap<String, Currency> CURRENCIES = new ConcurrentHashMap<>();

    private final String currencyCode;

    BaseCurrency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean isValid() {
        return currencyCode.length() == CURRENCY_CODE_LENGTH;
    }

    @Override
    public String getCurrencyCode() {
        return currencyCode;
    }

    public static Currency valueOf(String currencyCode) {
        Objects.requireNonNull(currencyCode, "Currency code cannot be null");
        Validator.validateCurrencyCode(currencyCode);

        final String code = currencyCode.toUpperCase();
        Currency currency = CURRENCIES.get(code);
        if (currency == null) {
            currency = CURRENCIES.computeIfAbsent(code, k -> new BaseCurrency(code));
        }
        return currency;
    }

    public static Currency getInvalid() {
        return InvalidCurrency.getInstance();
    }

    public static Currency getDefault() {
        return valueOf("INR");
    }
}
