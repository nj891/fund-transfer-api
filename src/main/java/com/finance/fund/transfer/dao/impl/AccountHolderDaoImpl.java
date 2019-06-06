package com.finance.fund.transfer.dao.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.finance.fund.transfer.dao.PagedResultDao;
import com.finance.fund.transfer.dao.AccountHolderDao;
import com.finance.fund.transfer.dto.AbstractAccountHolder;
import com.finance.fund.transfer.dto.AccountHolder;

/**
 * @author Nilesh
 */
public final class AccountHolderDaoImpl implements AccountHolderDao {

    private final AtomicLong counter = new AtomicLong(0L);
    private final ConcurrentMap<Long, AccountHolder> accountHolders = new ConcurrentHashMap<>();
    private final Long ourBankId;

    public AccountHolderDaoImpl() {
        final AccountHolder ourBank = addCorporate("AVS Technologies");
        ourBankId = ourBank.getId();
    }

    @Override
    public AccountHolder addCorporate(String name) {
        final AccountHolder corporate = AbstractAccountHolder.makeCorporate(counter.incrementAndGet(), name);
        accountHolders.putIfAbsent(corporate.getId(), corporate);
        return corporate;
    }

    @Override
    public AccountHolder addPersonal(String firstName, String lastName) {
        final AccountHolder personal = AbstractAccountHolder.makePersonal(counter.incrementAndGet(), firstName, lastName);
        accountHolders.putIfAbsent(personal.getId(), personal);
        return personal;
    }

    @Override
    public AccountHolder getById(Long id) {
        return accountHolders.getOrDefault(id, getInvalid());
    }

    @Override
    public AccountHolder getOurBank() {
        return getById(ourBankId);
    }

    @Override
    public PagedResultDao<AccountHolder> getAll(int pageNumber, int recordsPerPage) {
        return PagedResultDaoImpl.from(pageNumber, recordsPerPage, accountHolders);
    }

    @Override
    public AccountHolder getInvalid() {
        return AbstractAccountHolder.getInvalid();
    }

    @Override
    public int size() {
        return accountHolders.size();
    }
}
