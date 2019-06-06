package com.finance.fund.transfer.dao.impl;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.dao.AccountDao;
import com.finance.fund.transfer.dao.AccountHolderDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.AccountHolder;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Nilesh
 */
class AccountsDaoImplTest {

    @Test
    void getInitialBalance() {
        final AccountDao accountDao = Context.create().getAccountsDao();
        assertEquals(BigDecimal.valueOf(100_000_000.00d), accountDao.getInitialBalance());
    }

    @Test
    void getOurBankMainAccount() {
        final AccountDao accountDao = Context.create().getAccountsDao();
        final Account account = accountDao.getOurBankMainAccount();
        assertNotNull(account);
        assertTrue(account.isValid());
        assertTrue(account.isActive());
        assertEquals(accountDao.getInitialBalance(), account.getBalance());
    }

    @Test
    void size() {
        final AccountDao accountDao = Context.create().getAccountsDao();
        assertEquals(1, accountDao.size());
        accountDao.addOurBankAccount("20202810100000012345", BigDecimal.ZERO);
        assertEquals(2, accountDao.size());
    }

    @Test
    void getByHolder() {
        final Context context = Context.create();
        final AccountDao accountDao = context.getAccountsDao();
        final AccountHolderDao accountHolderDao = context.getAccountHolderDao();
        final AccountHolder accountHolder = accountHolderDao.addCorporate("test");

        Collection<Account> accounts = accountDao.getByHolder(accountHolder);
        assertNotNull(accounts);
        assertEquals(0, accounts.size());

        final Account clientAccount = accountDao.addPassiveAccount("40702810001234567890", accountHolder);
        accounts = accountDao.getByHolder(accountHolder);
        assertEquals(1, accounts.size());
        assertEquals(clientAccount, accounts.iterator().next());

        accounts = accountDao.getByHolder(accountHolderDao.getOurBank());
        assertEquals(1, accounts.size());
        assertEquals(accountDao.getOurBankMainAccount(), accounts.iterator().next());
    }
}