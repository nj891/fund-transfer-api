package com.finance.fund.transfer.dto;

import com.finance.fund.transfer.constants.AccountHolderType;
import com.finance.fund.transfer.dao.Identifiable;

/**
 * @author Nilesh
 */
public interface AccountHolder extends Identifiable {
	
    String getName();

    boolean isPersonal();

    boolean isCorporate();

    AccountHolderType getAccountHolderType();

}
