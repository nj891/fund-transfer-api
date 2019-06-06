package com.finance.fund.transfer.dto;

import com.finance.fund.transfer.constants.AccountHolderType;
import com.finance.fund.transfer.dao.Identifiable;

/**
 * @author Nilesh
 */
final class InvalidAccountHolder extends AbstractAccountHolder {
    private InvalidAccountHolder() {
        super(Identifiable.INVALID_ID, AccountHolderType.CORPORATE);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int hashCode() {
        return (int) Identifiable.INVALID_ID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (this == obj || obj instanceof InvalidAccountHolder);
    }

    private static class LazyHolder {
        private static final InvalidAccountHolder INSTANCE = new InvalidAccountHolder();
    }

    static InvalidAccountHolder getInstance() {
        return LazyHolder.INSTANCE;
    }
}
