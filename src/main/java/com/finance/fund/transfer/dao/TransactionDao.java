package com.finance.fund.transfer.dao;

import java.math.BigDecimal;

import com.finance.fund.transfer.dto.Account;
import com.finance.fund.transfer.dto.Transaction;

/**
 * @author Nilesh
 */
public interface TransactionDao extends BaseDao<Transaction> {

    Transaction add(Account debit, Account credit, BigDecimal amount);

    PagedResultDao<Transaction> getByAccount(Account account, int pageNumber, int recordsPerPage);

    default PagedResultDao<Transaction> getByAccount(Account account, Pagination pagination) {
        return getByAccount(account, pagination.getPageNumber(), pagination.getRecordsPerPage());
    }
}
