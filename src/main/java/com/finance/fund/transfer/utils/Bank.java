package com.finance.fund.transfer.utils;

import lombok.Getter;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.finance.fund.transfer.dao.BaseDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.Transaction;

/**
 * @author Nilesh
 */
public final class Bank {
    @Getter
    private final Context context;

    private Bank(Context context) {
        this.context = context;
    }

    public void generateData() {
        DataGeneratorUtils.getInstance(context)
                .withAccountHoldersCount(100)
                .withAccountsPerClient(2)
                .withClientTransactions(10_000)
                .generate();
    }

    public Transaction transfer(TransactionPayload payload) {
        Objects.requireNonNull(payload, "Transaction data cannot be null");

        final BaseDao<Account> baseDao = context.getAccountsDao();
        final Account debit = baseDao.getById(payload.getDebitAccountNumber());
        validateAccount(debit, payload.getDebitAccountNumber());
        final Account credit = baseDao.getById(payload.getCreditAccountNumber());
        validateAccount(credit, payload.getCreditAccountNumber());

        final Transaction transaction = context.getTransactionDao().add(debit, credit, payload.getAmount());
        transaction.run();
        return transaction;
    }

    private void validateAccount(Account account, Long id) {
        if (account.isNotValid()) {
            throw new NoSuchElementException(String.format("Account with Account Number %d is not found", id));
        }
    }

    private static class LazyHolder {
        static final Bank INSTANCE = new Bank(Context.create());
    }

    public static Bank getInstance() {
        return LazyHolder.INSTANCE;
    }
}
