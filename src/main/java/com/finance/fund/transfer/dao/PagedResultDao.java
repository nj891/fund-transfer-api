package com.finance.fund.transfer.dao;

import java.util.Collection;

/**
 * @author Nilesh
 */
public interface PagedResultDao<T> {

    boolean hasMore();

    Collection<T> getContent();

    int getPageNumber();

    int getRecordsPerPage();
}
