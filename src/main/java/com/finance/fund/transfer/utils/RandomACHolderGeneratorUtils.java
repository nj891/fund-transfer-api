package com.finance.fund.transfer.utils;

import org.apache.commons.lang3.StringUtils;

import com.finance.fund.transfer.dao.AccountHolderDao;
import com.finance.fund.transfer.dao.impl.Context;
import com.finance.fund.transfer.dto.AccountHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Nilesh
 */
class RandomACHolderGeneratorUtils extends AbstractUtils {
    private final int accountHolderCount;

    RandomACHolderGeneratorUtils(final Context context, final int accountHolderCount) {
        super(context, "accountholders");
        this.accountHolderCount = accountHolderCount;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(accountHolderCount);
        for (int i = 0; i < accountHolderCount; ++i) {
            futures.add(threadPool.submit(this::generateAccountHolder));
        }
        return futures;
    }

    private void generateAccountHolder() {
        final AccountHolderDao accountHolderDao = context.getAccountHolderDao();
        final int idx = counter.incrementAndGet();
        if (idx % 2 == 0) {
            AccountHolder accountHolder = accountHolderDao.addCorporate(generateCompanyName(idx));
            ids.add(accountHolder.getId());
        } else {
            AccountHolder pt = accountHolderDao.addPersonal(generateFirstName(idx), "Ajay");
            ids.add(pt.getId());
        }
    }

    private static String generateCompanyName(final int idx) {
        if (idx % 4 == 0) {
            return "GTS" + idx;
        } else if (idx % 6 == 0) {
		    return "CBSI" + idx;
		}
        return "Conjoin" + idx;
    }

    private static String generateFirstName(final int idx) {
        if (idx % 3 == 0) {
            return "Kamal" + idx;
        } else if (idx % 5 == 0) {
		    return "Rahul" + idx;
		} else if (idx % 7 == 0) {
		    return "Vijay" + idx;
		}
        return "Akshay" + idx;
    }
}
