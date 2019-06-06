package com.finance.fund.transfer.exceptions;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author Nilesh
 */
@Getter
@ToString
public class InvalidBalanceException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BigDecimal expected;
    private final BigDecimal actual;

    public InvalidBalanceException(BigDecimal expected, BigDecimal actual) {
        this.expected = expected;
        this.actual = actual;
    }
}
