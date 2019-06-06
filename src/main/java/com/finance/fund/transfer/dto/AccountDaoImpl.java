package com.finance.fund.transfer.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.finance.fund.transfer.constants.AccountMetadata;

/**
 * @author Nilesh
 */
public final class AccountDaoImpl extends AbstractAccount {

    private final AccountMetadata accountMetadata;

    private AccountDaoImpl(Long accountNumber, AccountMetadata accountMetadata, Currency currency,
                           String number, AccountHolder holder, boolean active, BigDecimal balance) {
        super(accountNumber, currency, number, holder, active, balance);
        Objects.requireNonNull(accountMetadata, "Account Meta-Data cannot be null");
        validateNumber(number);
        this.accountMetadata = accountMetadata;
    }

    private AccountDaoImpl(Long accountNumber, AccountMetadata accountMetadata, Currency currency,
                           String number, AccountHolder holder, boolean active) {
        this(accountNumber, accountMetadata, currency, number, holder, active, BigDecimal.ZERO);
    }

    public final AccountMetadata getAccountMetadata() {
        return accountMetadata;
    }

    @Override
    public String toString() {
        final String base = super.toString();
        return base.replace("Account{", "Account{").replaceFirst("(?s)(.*)}", "$1" + String.format(", accountMetadata=%s}", accountMetadata));
    }

    private static void validateNumber(String number) {
        if (number.length() != 20) {
            throw new IllegalArgumentException("AbstractAccount number must contain 20 characters");
        }
    }

    public static AccountDaoImpl makeActiveBalance(Long accountNumber, Currency currency, String number, AccountHolder holder, BigDecimal balance) {
        return new AccountDaoImpl(accountNumber, AccountMetadata.BALANCE, currency, number, holder, true, balance);
    }

    public static AccountDaoImpl makeActiveBalance(Long accountNumber, Currency currency, String number, AccountHolder holder) {
        return new AccountDaoImpl(accountNumber, AccountMetadata.BALANCE, currency, number, holder, true);
    }

    public static AccountDaoImpl makePassiveBalance(Long accountNumber, Currency currency, String number, AccountHolder holder) {
        return new AccountDaoImpl(accountNumber, AccountMetadata.BALANCE, currency, number, holder, false);
    }

    public static AccountDaoImpl makeActiveAcount(Long accountNumber, String number, AccountHolder holder) {
        return makeActiveBalance(accountNumber, BaseCurrency.getDefault(), number, holder);
    }

    public static AccountDaoImpl makePassiveAcount(Long accountNumber, String number, AccountHolder holder) {
        return makePassiveBalance(accountNumber, BaseCurrency.getDefault(), number, holder);
    }
}
