package com.finance.fund.transfer.dto;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

import com.finance.fund.transfer.dao.Identifiable;

/**
 * @author Nilesh
 */
public interface Account extends Identifiable {

    String getAccountNumber();

    Currency getAccountCurrency();

    BigDecimal getBalance();

    boolean debit(BigDecimal amount);

    boolean credit(BigDecimal amount);

    AccountHolder getHolder();

    boolean isActive();

    Lock writeLock();
}
