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
class CorporateAccountTest {

    @Test
    void attributes() {
        final AccountHolder accountHolder = AbstractAccountHolder.makeCorporate(11L, "test");
        assertNotNull(accountHolder);
        assertEquals(Long.valueOf(11L), accountHolder.getId());
        assertTrue(accountHolder.isValid());
        assertFalse(accountHolder.isNotValid());
        assertTrue(accountHolder.isCorporate());
        assertFalse(accountHolder.isPersonal());
        assertEquals(AccountHolderType.CORPORATE, accountHolder.getAccountHolderType());
        assertEquals("test", accountHolder.getName());
    }
}