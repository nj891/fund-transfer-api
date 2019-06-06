package com.finance.fund.transfer.dto;

import com.finance.fund.transfer.dao.Validatable;

/**
 * @author Nilesh
 */
public interface Currency extends Validatable {
    int CURRENCY_CODE_LENGTH = 3;

    String getCurrencyCode();
}
