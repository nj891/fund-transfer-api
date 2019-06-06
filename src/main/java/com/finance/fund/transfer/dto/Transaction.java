package com.finance.fund.transfer.dto;

import java.math.BigDecimal;

import com.finance.fund.transfer.constants.TransactionStatus;
import com.finance.fund.transfer.dao.Identifiable;

/**
 * @author Nilesh
 */
public interface Transaction extends Identifiable {
    Account getDebit();

    Account getCredit();

    BigDecimal getAmount();

    TransactionStatus getState();

    boolean run();
}
