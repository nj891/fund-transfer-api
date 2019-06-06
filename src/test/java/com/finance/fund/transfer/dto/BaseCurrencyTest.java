package com.finance.fund.transfer.dto;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.dto.BaseCurrency;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nilesh
 */
class BaseCurrencyTest {

    @Test
    void valueOf() {
        final Currency inr = BaseCurrency.valueOf("INR");
        assertNotNull(inr);
        assertTrue(inr.isValid());
        assertFalse(inr.isNotValid());
        assertEquals("INR", inr.getCurrencyCode());
        assertEquals(BaseCurrency.valueOf("INR"), inr);
        assertSame(BaseCurrency.valueOf("INR"), inr);

        final Currency usd = BaseCurrency.valueOf("USD");
        assertNotNull(usd);
        assertTrue(usd.isValid());
        assertFalse(usd.isNotValid());
        assertEquals("USD", usd.getCurrencyCode());
        assertEquals(BaseCurrency.valueOf("USD"), usd);
        assertSame(BaseCurrency.valueOf("USD"), usd);

        assertNotEquals(usd, inr);
    }

    @Test
    void defaultCurrency() {
        final Currency def = BaseCurrency.getDefault();
        assertNotNull(def);
        assertTrue(def.isValid());
        assertEquals("INR", def.getCurrencyCode());
    }
}