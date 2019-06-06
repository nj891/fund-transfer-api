package com.finance.fund.transfer.dto;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.constants.AccountHolderType;
import com.finance.fund.transfer.dto.AbstractAccountHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Nilesh
 */
class InvalidAccountHolderTest {

    @Test
    void invalidAccountHolder() {
        final AccountHolder accountHolder = AbstractAccountHolder.getInvalid();
        assertNotNull(accountHolder);

        assertFalse(accountHolder.isValid());
        assertTrue(accountHolder.isNotValid());
        assertEquals(Long.valueOf(-1), accountHolder.getId());

        assertEquals(AbstractAccountHolder.getInvalid(), accountHolder);
        assertEquals(-1, accountHolder.hashCode());

        assertEquals("", accountHolder.getName());
        assertEquals(AccountHolderType.CORPORATE, accountHolder.getAccountHolderType());
        assertTrue(accountHolder.isCorporate());
        assertFalse(accountHolder.isPersonal());
    }
}