package com.finance.fund.transfer.dao;

/**
 * @author Nilesh
 */
public interface BaseDao<T> extends Pageable<T> {

    int size();

    T getById(Long id);

    T getInvalid();
}
