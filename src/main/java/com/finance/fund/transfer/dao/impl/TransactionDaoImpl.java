package com.finance.fund.transfer.dao.impl;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import com.finance.fund.transfer.dao.PagedResultDao;
import com.finance.fund.transfer.dao.TransactionDao;
import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.MoneyTransaction;
import com.finance.fund.transfer.dto.Transaction;

/**
 * @author Nilesh
 */
final class TransactionDaoImpl implements TransactionDao {

    private final AtomicLong counter = new AtomicLong(0L);
    private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Transaction getById(Long transactionId) {
        return transactions.getOrDefault(transactionId, getInvalid());
    }

    @Override
    public Transaction getInvalid() {
        return MoneyTransaction.getInvalid();
    }

    @Override
    public int size() {
        return transactions.size();
    }

    @Override
    public Transaction add(Account debit, Account credit, BigDecimal amount) {
        final Transaction transaction = MoneyTransaction.make(counter.incrementAndGet(), debit, credit, amount);
        transactions.putIfAbsent(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public PagedResultDao<Transaction> getAll(int pageNumber, int recordsPerPage) {
        return PagedResultDaoImpl.from(pageNumber, recordsPerPage, transactions);
    }

    @Override
    public PagedResultDao<Transaction> getByAccount(Account account, int pageNumber, int recordsPerPage) {
        Objects.requireNonNull(account, "Account cannot be null");
        Predicate<Transaction> predicate = t -> t.getDebit().equals(account) || t.getCredit().equals(account);
        return PagedResultDaoImpl.from(pageNumber, recordsPerPage, transactions, predicate);
    }
}
