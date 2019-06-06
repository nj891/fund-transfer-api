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
class PersonalAccountTest {

    @Test
    void attributes() {
        final AccountHolder accountHolder = AbstractAccountHolder.makePersonal(15L,"First", "Last");
        assertNotNull(accountHolder);
        assertEquals(Long.valueOf(15L), accountHolder.getId());
        assertTrue(accountHolder.isValid());
        assertFalse(accountHolder.isNotValid());
        assertFalse(accountHolder.isCorporate());
        assertTrue(accountHolder.isPersonal());
        assertEquals(AccountHolderType.PERSONAL, accountHolder.getAccountHolderType());
        assertEquals("First Last", accountHolder.getName());
    }
}