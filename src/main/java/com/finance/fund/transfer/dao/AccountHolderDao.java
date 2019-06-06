package com.finance.fund.transfer.dao;

import com.finance.fund.transfer.dto.AccountHolder;

/**
 * @author Nilesh
 */
public interface AccountHolderDao extends BaseDao<AccountHolder> {

    AccountHolder addCorporate(String name);

    AccountHolder addPersonal(String firstName, String lastName);

    AccountHolder getOurBank();
}
