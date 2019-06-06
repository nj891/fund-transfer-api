package com.finance.fund.transfer.dto;

import lombok.Getter;

import java.util.Objects;

import com.finance.fund.transfer.constants.AccountHolderType;

/**
 * @author Nilesh
 */
public abstract class AbstractAccountHolder implements AccountHolder {
    @Getter
    private final Long id;

    @Getter
    private final AccountHolderType accountHolderType;

    AbstractAccountHolder(Long id, AccountHolderType accountHolderType) {
        Objects.requireNonNull(id, "Id cannot be null");
        Objects.requireNonNull(accountHolderType, "accountHolderType cannot be null");
        this.id = id;
        this.accountHolderType = accountHolderType;
    }

    public final boolean isPersonal() {
        return AccountHolderType.PERSONAL == accountHolderType;
    }

    public final boolean isCorporate() {
        return AccountHolderType.CORPORATE == accountHolderType;
    }
    
    @Override
    public final String toString() {
        return String.format("AccountHolder{%s, accountHolderType=%s, id=%d}",
                getName(), getAccountHolderType(), getId());
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public static AccountHolder makeCorporate(Long id, String name) {
        return new CorporateAccount(id, name);
    }

    public static AccountHolder makePersonal(Long id, String firstName, String lastName) {
        return new PersonalAccount(id, firstName, lastName);
    }

    public static AccountHolder getInvalid() {
        return InvalidAccountHolder.getInstance();
    }
}
