package com.finance.fund.transfer.dao.impl;

import com.finance.fund.transfer.dao.AccountDao;
import com.finance.fund.transfer.dao.AccountHolderDao;
import com.finance.fund.transfer.dao.TransactionDao;

import lombok.Getter;

/**
 * @author Nilesh
 */
@Getter
public class Context {
    private final AccountHolderDao accountHolderDao;
    private final AccountDao accountsDao;
    private final TransactionDao transactionDao;

    private Context(AccountHolderDao accountHolderDao,
                   AccountDao accountsDao,
                   TransactionDao transactionDao) {
        this.accountHolderDao = accountHolderDao;
        this.accountsDao = accountsDao;
        this.transactionDao = transactionDao;
    }

    public static Context create() {
        final AccountHolderDao accountHolderDao = new AccountHolderDaoImpl();
        final AccountDao accountsDao = new AccountsDaoImpl(accountHolderDao);
        final TransactionDao transactionDao = new TransactionDaoImpl();
        return new Context(accountHolderDao, accountsDao, transactionDao);
    }
}
