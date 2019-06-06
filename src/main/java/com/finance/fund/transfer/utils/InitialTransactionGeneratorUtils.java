package com.finance.fund.transfer.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.finance.fund.transfer.dao.AccountDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.Transaction;

/**
 * @author Nilesh
 */
class InitialTransactionGeneratorUtils extends AbstractUtils {
    private final List<Long> accountIds;
    private final boolean runImmediately;

    InitialTransactionGeneratorUtils(Context context, List<Long> accountIds, boolean runImmediately) {
        super(context, "initial transactions");
        Objects.requireNonNull(accountIds, "Ids list cannot be null");
        this.accountIds = accountIds;
        this.runImmediately = runImmediately;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(accountIds.size());
        for (Long accountId : accountIds) {
            Runnable runnableTask = () -> generateInitialTransaction(accountId);
            futures.add(threadPool.submit(runnableTask));
        }
        return futures;
    }

    private void generateInitialTransaction(final Long creditAccountNumber) {
        final AccountDao accountsDao = context.getAccountsDao();
        final Account debit = accountsDao.getOurBankMainAccount();
        final Account credit = accountsDao.getById(creditAccountNumber);
        if (credit.isValid()) {
            final BigDecimal amount = TransactionUtils.generateAmount(500_000, 1000_000);
            final Transaction transaction = context.getTransactionDao().add(debit, credit, amount);
            if (runImmediately) {
                transaction.run();
            }
            ids.add(transaction.getId());
        } else {
            logger.error("Credit account with Account Number = {} not found", creditAccountNumber);
        }
    }
}
