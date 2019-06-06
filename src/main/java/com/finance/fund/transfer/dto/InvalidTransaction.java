package com.finance.fund.transfer.dto;

import java.math.BigDecimal;

import com.finance.fund.transfer.dao.Identifiable;

/**
 * @author Nilesh
 */
final class InvalidTransaction extends MoneyTransaction {
    private InvalidTransaction() {
        super(Identifiable.INVALID_ID, AbstractAccount.getInvalid(), AbstractAccount.getInvalid(), BigDecimal.ZERO);
    }

    @Override
    public int hashCode() {
        return (int) Identifiable.INVALID_ID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (this == obj || obj instanceof InvalidTransaction);
    }

    @Override
    public boolean run() {
        return false;
    }

    private static class LazyHolder {
        private static final InvalidTransaction INSTANCE = new InvalidTransaction();
    }

    static InvalidTransaction getInstance() {
        return LazyHolder.INSTANCE;
    }
}
