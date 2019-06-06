package com.finance.fund.transfer.dao;

import java.math.BigDecimal;
import java.util.Collection;

import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.AccountHolder;
import com.finance.fund.transfer.dto.Currency;

/**
 * @author Nilesh
 */
public interface AccountDao extends BaseDao<Account> {

    Account addOurBankAccount(Currency currency, String number, BigDecimal balance);

    Account addOurBankAccount(String number, BigDecimal balance);

    Account getOurBankMainAccount();

    Account addPassiveAccount(Currency currency, String number, AccountHolder holder);

    Account addPassiveAccount(String number, AccountHolder holder);

    BigDecimal getInitialBalance();

    void validateBalance();

    Collection<Account> getByHolder(AccountHolder holder);
}
