package com.finance.fund.transfer.dto;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.dto.AbstractAccount;
import com.finance.fund.transfer.dto.AbstractAccountHolder;
import com.finance.fund.transfer.dto.BaseCurrency;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Nilesh
 */
class InvalidAccountTest {

    @Test
    void invalidAccount() {
        final Account a = AbstractAccount.getInvalid();
        assertNotNull(a);

        assertFalse(a.isValid());
        assertTrue(a.isNotValid());
        assertEquals(Long.valueOf(-1), a.getId());

        assertEquals(AbstractAccount.getInvalid(), a);
        assertEquals(-1, a.hashCode());

        assertEquals("", a.getAccountNumber());
        assertEquals(BigDecimal.valueOf(0), a.getBalance());
        assertEquals(BaseCurrency.getInvalid(), a.getAccountCurrency());
        assertEquals(AbstractAccountHolder.getInvalid(), a.getHolder());
        assertFalse(a.isActive());
    }

    @Test
    void toStringImpl() {
        final Account a = AbstractAccount.getInvalid();
        assertNotNull(a);
        assertTrue(a.toString().startsWith("InvalidAccount{"));
    }
}