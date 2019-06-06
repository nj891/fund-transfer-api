package com.finance.fund.transfer.dto;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.dto.BaseCurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Nilesh
 */
class InvalidCurrencyTest {

    @Test
    void invalidCurrency() {
        final Currency currency = BaseCurrency.getInvalid();
        assertNotNull(currency);

        assertFalse(currency.isValid());
        assertTrue(currency.isNotValid());

        assertEquals(BaseCurrency.getInvalid(), currency);
        assertEquals(-1, currency.hashCode());

        assertEquals("", currency.getCurrencyCode());
    }
}