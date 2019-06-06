package com.finance.fund.transfer.utils;

import org.apache.commons.lang3.tuple.Pair;

import com.finance.fund.transfer.dao.AccountDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Nilesh
 */
class RandomTransactionGeneratorUtils extends AbstractUtils {
    private final int trnCount;
    private final List<Long> accountIds;
    private final boolean runImmediately;

    RandomTransactionGeneratorUtils(final Context context, final List<Long> accountIds,
                               final boolean runImmediately, final int threadPoolSize, int trnCount) {
        super(context, "clients transactions", threadPoolSize);
        Objects.requireNonNull(accountIds, "Ids list cannot be null");
        this.accountIds = accountIds;
        this.runImmediately = runImmediately;
        this.trnCount = trnCount;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(trnCount);
        for (int i = 0; i < trnCount; ++i) {
            futures.add(threadPool.submit(this::generateTransaction));
        }
        return futures;
    }

    private void generateTransaction() {
        final AccountDao accountsDao = context.getAccountsDao();
        final Pair<Long, Long> randomIds = TransactionUtils.getRandomAccountIds(accountIds);
        final Account debit = accountsDao.getById(randomIds.getLeft());
        if (debit.isValid()) {
            final Account credit = accountsDao.getById(randomIds.getRight());
            if (credit.isValid()) {
                final BigDecimal amount = TransactionUtils.generateAmount(5_000, 100_000);
                final Transaction transaction = context.getTransactionDao().add(debit, credit, amount);
                if (runImmediately) {
                    transaction.run();
                }
                ids.add(transaction.getId());
            } else {
                logger.error("Credit account with Account Number = {} not found", randomIds.getRight());
            }
        } else {
            logger.error("Debit account with Account Number = {} not found", randomIds.getLeft());
        }
    }
}
