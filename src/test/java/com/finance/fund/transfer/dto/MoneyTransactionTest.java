package com.finance.fund.transfer.dto;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.dao.AccountDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.MoneyTransaction;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Nilesh
 */
class MoneyTransactionTest {

    @Test
    void constructorWithNulls() {
        final AccountDao accountDao = Context.create().getAccountsDao();

        NullPointerException e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(null, null, null, null));
        assertEquals("Transaction Id cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, null, null, null));
        assertEquals("Debit account cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, accountDao.getInvalid(), null, null));
        assertEquals("Credit account cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, accountDao.getInvalid(), accountDao.getInvalid(), null));
        assertEquals("Amount cannot be null", e.getLocalizedMessage());
    }

    @Test
    void constructorWithInvalidValues() {
        final AccountDao accountDao = Context.create().getAccountsDao();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, accountDao.getInvalid(), accountDao.getInvalid(), BigDecimal.valueOf(-1)));
        assertEquals("Amount must be greater than zero", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, accountDao.getInvalid(), accountDao.getInvalid(), BigDecimal.valueOf(0)));
        assertEquals("Amount must be greater than zero", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, accountDao.getInvalid(), accountDao.getInvalid(), BigDecimal.ONE));
        assertEquals("Debit account must be valid", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, accountDao.getOurBankMainAccount(), accountDao.getInvalid(), BigDecimal.ONE));
        assertEquals("Credit account must be valid", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, accountDao.getOurBankMainAccount(), accountDao.getOurBankMainAccount(), BigDecimal.ONE));
        assertEquals("Accounts must be different", e.getLocalizedMessage());
    }
}