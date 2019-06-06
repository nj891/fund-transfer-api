package com.finance.fund.transfer.utils;

import org.apache.commons.lang3.StringUtils;

import com.finance.fund.transfer.dao.AccountDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.AccountHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Nilesh
 */
class RandomAccountGeneratorUtils extends AbstractUtils {
    private final int accountsPerClient;
    private final List<Long> accountHolderIds;

    RandomAccountGeneratorUtils(final Context context, final List<Long> accountHolderIds, final int accountsPerClient) {
        super(context, "accounts");
        Objects.requireNonNull(accountHolderIds, "Ids list cannot be null");
        this.accountHolderIds = accountHolderIds;
        this.accountsPerClient = accountsPerClient;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(accountHolderIds.size() * accountsPerClient);
        for (Long accountHolderId : accountHolderIds) {
            Runnable runnableTask = () -> generateAccount(accountHolderId);
            futures.add(threadPool.submit(runnableTask));
        }
        return futures;
    }

    private void generateAccount(Long accountHolderId) {
        final AccountDao accountsDao = context.getAccountsDao();
        final AccountHolder accountHolder = context.getAccountHolderDao().getById(accountHolderId);
        if (accountHolder.isValid()) {
            for (int i = 0; i < accountsPerClient; ++i) {
                final int idx = counter.incrementAndGet();
                final Account account = accountsDao.addPassiveAccount(generateNumber(idx), accountHolder);
                ids.add(account.getId());
            }
        } else {
            logger.error("AccountHolder with id = {} not found", accountHolderId);
        }
    }

    private static String generateNumber(final int idx) {
        return "4080281010" + StringUtils.leftPad(String.valueOf(idx), 10, '0');
    }
}
