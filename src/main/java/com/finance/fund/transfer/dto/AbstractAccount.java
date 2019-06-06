package com.finance.fund.transfer.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finance.fund.transfer.constants.TransferConstants;
import com.finance.fund.transfer.utils.Validator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Nilesh
 */
public abstract class AbstractAccount implements Account {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAccount.class);

    private final Long accountNumber;
    private final Currency currency;
    private final String number;
    private final AccountHolder holder;
    private final boolean active;
    private BigDecimal balance;
    private final transient Lock lock;

    AbstractAccount(Long accountNumber, Currency currency, String number,
                    AccountHolder holder, boolean active, BigDecimal balance) {
        Objects.requireNonNull(accountNumber, "accountNumber cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        Objects.requireNonNull(number, "Number cannot be null");
        Objects.requireNonNull(holder, "Holder cannot be null");
        Objects.requireNonNull(balance, "Balance cannot be null");
        Validator.validateAmountNotNegative(balance);

        this.accountNumber = accountNumber;
        this.currency = currency;
        this.number = number;
        this.holder = holder;
        this.active = active;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    @Override
    public final Long getId() {
        return accountNumber;
    }

    @Override
    public final String getAccountNumber() {
        return number;
    }

    @Override
    public final Currency getAccountCurrency() {
        return currency;
    }

    @Override
    public final BigDecimal getBalance() {
        try {
            lock.lock();
            return balance;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean debit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Validator.validateAmountNotNegative(amount);

        try {
            if (lock.tryLock(TransferConstants.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    if (balance.compareTo(amount) > 0) {
                        balance = balance.subtract(amount);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean credit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Validator.validateAmountNotNegative(amount);

        try {
            if (lock.tryLock(TransferConstants.ACCOUNT_WAIT_INTERVAL, TimeUnit.MILLISECONDS)) {
                try {
                    balance = balance.add(amount);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    @Override
    public final AccountHolder getHolder() {
        return holder;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Lock writeLock() {
        return lock;
    }

    @Override
    public String toString() {
        return String.format("Account{accountNumber=%d, currency=%s, number=%s, active=%s, balance=%s, holder=%s}",
        		accountNumber, currency, number, active, balance, holder);
    }

    public static Account getInvalid() {
        return InvalidAccount.getInstance();
    }

    public static Account makeActiveAccount(Long id, Currency currency, String number, AccountHolder holder, BigDecimal balance) {
        return AccountDaoImpl.makeActiveBalance(id, currency, number, holder, balance);
    }

    public static Account makeActiveAccount(Long id, Currency currency, String number, AccountHolder holder) {
        return AccountDaoImpl.makeActiveBalance(id, currency, number, holder);
    }

    public static Account makePassiveAccount(Long id, Currency currency, String number, AccountHolder holder) {
        return AccountDaoImpl.makeActiveBalance(id, currency, number, holder);
    }

    public static Account makeActiveAccount(Long id, String number, AccountHolder holder) {
        return AccountDaoImpl.makeActiveAcount(id, number, holder);
    }

    public static Account makePassiveAccount(Long id, String number, AccountHolder holder) {
        return AccountDaoImpl.makeActiveAcount(id, number, holder);
    }
}
