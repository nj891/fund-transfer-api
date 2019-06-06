package com.finance.fund.transfer.dao;

/**
 * @author Nilesh
 */
public interface Validatable {
    boolean isValid();

    default boolean isNotValid() {
        return !isValid();
    }
}
