package com.finance.fund.transfer.dao;

/**
 * @author Nilesh
 */
public interface Identifiable extends Validatable {
    long INVALID_ID = -1L;

    Long getId();

    @Override
    default boolean isValid() {
        return INVALID_ID != getId();
    }
}
