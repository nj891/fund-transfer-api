package com.finance.fund.transfer.dto;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.constants.AccountMetadata;
import com.finance.fund.transfer.dao.AccountHolderDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.BaseCurrency;
import com.finance.fund.transfer.dto.AccountDaoImpl;
import com.finance.fund.transfer.utils.JsonUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Nilesh
 */
class AccountTest {

    private final AccountHolderDao accountHolderDao = Context.create().getAccountHolderDao();

    @Test
    void getAccountHolder() {
        AccountDaoImpl a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(AccountMetadata.BALANCE, a.getAccountMetadata());
    }

    @Test
    void getId() {
        final Account a = makeAccount(11L);
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(Long.valueOf(11L), a.getId());
    }

    @Test
    void getNumber() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals("30102810100000000001", a.getAccountNumber());
    }

    @Test
    void getCurrency() {
        Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(BaseCurrency.getDefault(), a.getAccountCurrency());

        a = AccountDaoImpl.makePassiveBalance(1L, BaseCurrency.valueOf("USD"), "30102810100000000001", accountHolderDao.getOurBank());
        assertNotNull(a);
        assertTrue(a.isValid());
        assertFalse(a.isActive());
        assertEquals(BaseCurrency.valueOf("USD"), a.getAccountCurrency());
    }

    @Test
    void getBalance() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(BigDecimal.valueOf(0L), a.getBalance());
    }

    @Test
    void getHolder() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(accountHolderDao.getOurBank(), a.getHolder());
    }

    @Test
    void debitAndCredit() {
        final Account a = makeAccount();
        assertFalse(a.debit(BigDecimal.TEN));
        assertTrue(a.credit(BigDecimal.TEN));
        assertEquals(BigDecimal.TEN, a.getBalance());
        assertTrue(a.debit(BigDecimal.ONE));
        assertEquals(BigDecimal.valueOf(9), a.getBalance());
    }

    @Test
    void toStringImpl() {
        final Account a = makeAccount();
        assertTrue(a.toString().startsWith("Account{"));
        assertTrue(a.toString().contains(", accountMetadata="));
        assertEquals("Account{accountNumber=1, currency=BaseCurrency(currencyCode=INR), number=30102810100000000001, active=true, balance=0, holder=AccountHolder{AVS Technologies, accountHolderType=CORPORATE, id=1}, accountMetadata=BALANCE}",
                a.toString());
    }

    @Test
    void lockShouldBeTransient() {
        final Account a = makeAccount();
        final String json = JsonUtils.make().toJson(a);
        assertNotNull(json);
        assertFalse(json.contains("lock"));
        assertEquals("{\n" +
                "  \"accountMetadata\": \"BALANCE\",\n" +
                "  \"accountNumber\": 1,\n" +
                "  \"currency\": {\n" +
                "    \"currencyCode\": \"INR\"\n" +
                "  },\n" +
                "  \"number\": \"30102810100000000001\",\n" +
                "  \"holder\": {\n" +
                "    \"name\": \"AVS Technologies\",\n" +
                "    \"id\": 1,\n" +
                "    \"accountHolderType\": \"CORPORATE\"\n" +
                "  },\n" +
                "  \"active\": true,\n" +
                "  \"balance\": 0\n" +
                "}", json);
    }

    private AccountDaoImpl makeAccount() {
        return makeAccount(1L);
    }

    private AccountDaoImpl makeAccount(Long accountNumber) {
        return AccountDaoImpl.makeActiveAcount(accountNumber, "30102810100000000001", accountHolderDao.getOurBank());
    }
}