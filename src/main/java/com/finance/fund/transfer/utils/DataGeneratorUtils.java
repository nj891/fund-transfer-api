package com.finance.fund.transfer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finance.fund.transfer.dao.impl.Context;

import java.util.List;

/**
 * @author Nilesh
 */
public class DataGeneratorUtils {
    private static final Logger logger = LoggerFactory.getLogger(DataGeneratorUtils.class);

    private final Context context;
    private int accountHolderCount = 100_000;
    private int accountsPerClient = 10;
    private boolean initialTransactions = true;
    private boolean runImmediately = true;
    private int clientTransactionsCount = 1_000_000;

    private DataGeneratorUtils(Context context) {
        this.context = context;
    }

    public DataGeneratorUtils withAccountHoldersCount(final int count) {
    	accountHolderCount = count;
        return this;
    }

    public DataGeneratorUtils withAccountsPerClient(final int count) {
        this.accountsPerClient = count;
        return this;
    }

    public DataGeneratorUtils withoutInitialTransactions() {
        this.initialTransactions = false;
        return this;
    }

    public DataGeneratorUtils withoutRunningTransactions() {
        this.runImmediately = false;
        return this;
    }

    public DataGeneratorUtils withClientTransactions(final int count) {
        this.clientTransactionsCount = count;
        if (count > 0) {
            initialTransactions = true;
        }
        return this;
    }

    public DataGeneratorUtils withoutClientTransactions() {
        this.clientTransactionsCount = 0;
        return this;
    }

    public void generate() {
        try {
            final List<Long> acountNumbers = generateAccountHolders();
            final List<Long> accountIds = generateAccounts(acountNumbers);
            if (initialTransactions) {
                generateInitialTransactions(acountNumbers);
                if (clientTransactionsCount > 0) {
                    generateClientTransactions(acountNumbers);
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private List<Long> generateAccountHolders() {
        final AbstractUtils accountGenerator = new RandomACHolderGeneratorUtils(context, accountHolderCount);
        final List<Long> accountHolders = accountGenerator.generate();
        logger.debug("Account holders count = {}", accountHolders.size());
        return accountHolders;
    }

    private List<Long> generateAccounts(final List<Long> accountHolderIds) {
        final AbstractUtils accountGenerator = new RandomAccountGeneratorUtils(context, accountHolderIds, accountsPerClient);
        final List<Long> accountNumbers = accountGenerator.generate();
        logger.debug("Account ids count = {}", accountNumbers.size());
        return accountNumbers;
    }

    private void generateInitialTransactions(final List<Long> accountIds) {
        final AbstractUtils initialTransactionGenerator = new InitialTransactionGeneratorUtils(context, accountIds, runImmediately);
        final List<Long> initialTrnIds = initialTransactionGenerator.generate();
        logger.debug("Initial transaction ids count = {}", initialTrnIds.size());
        context.getAccountsDao().validateBalance();
    }

    private void generateClientTransactions(final List<Long> accountIds) {
        final AbstractUtils transactionGenerator = new RandomTransactionGeneratorUtils(
                context, accountIds, runImmediately, 10, clientTransactionsCount);
        final List<Long> trnIds = transactionGenerator.generate();
        logger.debug("Transaction ids count = {}", trnIds.size());
        context.getAccountsDao().validateBalance();
    }

    public static DataGeneratorUtils getInstance(Context context) {
        return new DataGeneratorUtils(context);
    }
}
