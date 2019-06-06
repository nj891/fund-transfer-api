package com.finance.fund.transfer.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.finance.fund.transfer.constants.AccountHolderType;

import java.util.Objects;

/**
 * @author Nilesh
 */
abstract class AbstractCorporate extends AbstractAccountHolder {
    private final String name;

    AbstractCorporate(Long accountNumber, String name) {
        super(accountNumber, AccountHolderType.CORPORATE);
        Objects.requireNonNull(name, "Name cannot be null");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(39, 19)
                .append(name)
                .append(getAccountHolderType())
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AbstractCorporate)) {
            return false;
        }

        AbstractCorporate other = (AbstractCorporate) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .append(getAccountHolderType(), other.getAccountHolderType())
                .isEquals();
    }
}
