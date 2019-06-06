package com.finance.fund.transfer.dto;

import java.math.BigDecimal;

import com.finance.fund.transfer.dao.Identifiable;

/**
 * @author Nilesh
 */
final class InvalidAccount extends AbstractAccount {

    private InvalidAccount() {
        super(Identifiable.INVALID_ID, BaseCurrency.getInvalid(), "", AbstractAccountHolder.getInvalid(), false, BigDecimal.ZERO);
    }

    @Override
    public int hashCode() {
        return (int) Identifiable.INVALID_ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return (obj instanceof InvalidAccount);
    }

    @Override
    public String toString() {
        final String base = super.toString();
        return base.replace("Account{", "InvalidAccount{");
    }

    private static class LazyHolder {
        private static final InvalidAccount INSTANCE = new InvalidAccount();
    }

    static Account getInstance() {
        return LazyHolder.INSTANCE;
    }
}
