package com.finance.fund.transfer.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Nilesh
 */
@Getter
@AllArgsConstructor
public final class TransactionPayload {
    private final Long debitAccountNumber;
    private final Long creditAccountNumber;
    private final BigDecimal amount;
}
