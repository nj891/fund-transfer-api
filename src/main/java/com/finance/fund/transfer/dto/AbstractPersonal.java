package com.finance.fund.transfer.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.finance.fund.transfer.constants.AccountHolderType;

import java.util.Objects;

/**
 * @author Nilesh
 */
abstract class AbstractPersonal extends AbstractAccountHolder {
    private final String firstName;
    private final String lastName;

    AbstractPersonal(Long accountNumber, String firstName, String lastName) {
        super(accountNumber, AccountHolderType.PERSONAL);
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(41, 11)
                .append(firstName)
                .append(lastName)
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

        if (!(obj instanceof AbstractPersonal)) {
            return false;
        }

        AbstractPersonal other = (AbstractPersonal) obj;
        return new EqualsBuilder()
                .append(firstName, other.firstName)
                .append(lastName, other.lastName)
                .append(getAccountHolderType(), other.getAccountHolderType())
                .isEquals();
    }
}
