package com.finance.fund.transfer.dao;

/**
 * @author Nilesh
 */
public interface Pageable<T> {

    PagedResultDao<T> getAll(int pageNumber, int recordsPerPage);

    default PagedResultDao<T> getAll(Pagination pagination) {
        return getAll(pagination.getPageNumber(), pagination.getRecordsPerPage());
    }
}
