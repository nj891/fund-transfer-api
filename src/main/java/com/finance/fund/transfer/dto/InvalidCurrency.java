package com.finance.fund.transfer.dto;

import com.finance.fund.transfer.dao.Identifiable;

/**
 * @author Nilesh
 */
final class InvalidCurrency extends BaseCurrency {
    private InvalidCurrency() {
        super("");
    }

    @Override
    public int hashCode() {
        return (int) Identifiable.INVALID_ID;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (this == obj || obj instanceof InvalidCurrency);
    }

    private static class LazyHolder {
        private static final InvalidCurrency INSTANCE = new InvalidCurrency();
    }

    static Currency getInstance() {
        return LazyHolder.INSTANCE;
    }
}
