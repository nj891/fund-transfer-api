package com.finance.fund.transfer.dao.impl;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.dao.AccountHolderDao;
import com.finance.fund.transfer.dao.impl.AccountHolderDaoImpl;
import com.finance.fund.transfer.dto.AccountHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Nilesh
 */
class AccountHolderDaoImplTest {

    @Test
    void getById() {
        final AccountHolderDao accountHolderDao = make();
        AccountHolder accountHolder = accountHolderDao.getById(1L);
        assertNotNull(accountHolder);
        assertTrue(accountHolder.isValid());

        accountHolder = accountHolderDao.getById(Long.MAX_VALUE);
        assertNotNull(accountHolder);
        assertFalse(accountHolder.isValid());
        assertEquals(accountHolderDao.getInvalid(), accountHolder);
    }

    @Test
    void getOurBank() {
        final AccountHolderDao accountHolderDao = make();
        final AccountHolder accountHolder = accountHolderDao.getOurBank();
        assertNotNull(accountHolder);
        assertTrue(accountHolder.isValid());
        assertTrue(accountHolder.isCorporate());
        assertEquals(Long.valueOf(1), accountHolder.getId());
        assertEquals("AVS Technologies", accountHolder.getName());
    }

    private AccountHolderDao make() {
        return new AccountHolderDaoImpl();
    }
}