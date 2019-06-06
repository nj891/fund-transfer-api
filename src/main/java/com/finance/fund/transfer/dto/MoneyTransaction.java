package com.finance.fund.transfer.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finance.fund.transfer.constants.TransactionStatus;
import com.finance.fund.transfer.constants.TransferConstants;
import com.finance.fund.transfer.utils.Validator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author Nilesh
 */
public class MoneyTransaction implements Transaction {
    private static final Logger logger = LoggerFactory.getLogger(MoneyTransaction.class);

    private final Long transactionId;
    private final Account debit;
    private final Account credit;
    private final BigDecimal amount;
    private TransactionStatus state;

    MoneyTransaction(Long transactionId, Account debit, Account credit, BigDecimal amount) {
        Objects.requireNonNull(transactionId, "Transaction Id cannot be null");
        Objects.requireNonNull(debit, "Debit account cannot be null");
        Objects.requireNonNull(credit, "Credit account cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Validator.validateAmountPositive(amount);
        Validator.validateAccountsAreValid(debit, credit);
        Validator.validateAccountIsDifferent(debit, credit);
        Validator.validateCurrencyIsTheSame(debit, credit);

        this.transactionId = transactionId;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
        this.state = TransactionStatus.NEW;
    }

    @Override
    public Long getId() {
        return transactionId;
    }

    @Override
    public Account getDebit() {
        return debit;
    }

    @Override
    public Account getCredit() {
        return credit;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public synchronized TransactionStatus getState() {
        return state;
    }

    @Override
    public synchronized boolean run() {
        if (state != TransactionStatus.COMPLETED) {
            changeState();
            return doRun();
        }
        return false;
    }

    private boolean doRun() {
        final Lock debitLock = debit.writeLock();
        try {
            if (debitLock.tryLock(TransferConstants.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    final Lock creditLock = credit.writeLock();
                    if (creditLock.tryLock(TransferConstants.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                        try {
                            if (debit.debit(amount) && credit.credit(amount)) {
							    state = TransactionStatus.COMPLETED;
							    logger.trace("Transaction {} completed", transactionId);
							    return true;
							}
                            state = TransactionStatus.INSUFFICIENT_FUNDS;
                        } finally {
                            creditLock.unlock();
                        }
                    } else {
                        state = TransactionStatus.CONCURRENCY_ERROR;
                    }
                } finally {
                    debitLock.unlock();
                }
            } else {
                state = TransactionStatus.CONCURRENCY_ERROR;
            }
        } catch (InterruptedException e) {
            state = TransactionStatus.CONCURRENCY_ERROR;
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    private void changeState() {
        switch (state) {
            case INSUFFICIENT_FUNDS:
            case CONCURRENCY_ERROR:
                state = TransactionStatus.RESTARTED;
                break;
        }
    }

    public static Transaction getInvalid() {
        return InvalidTransaction.getInstance();
    }

    public static Transaction make(Long transactionId, Account debit, Account credit, BigDecimal amount) {
        return new MoneyTransaction(transactionId, debit, credit, amount);
    }
}
