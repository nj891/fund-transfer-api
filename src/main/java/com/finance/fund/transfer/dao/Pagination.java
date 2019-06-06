package com.finance.fund.transfer.dao;

/**
 * @author Nilesh
 */
public interface Pagination extends Validatable {

    int getRecordsPerPage();

    int getPageNumber();
}
