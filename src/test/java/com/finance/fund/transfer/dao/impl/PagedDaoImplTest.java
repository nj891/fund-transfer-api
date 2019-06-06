package com.finance.fund.transfer.dao.impl;

import org.junit.jupiter.api.Test;

import com.finance.fund.transfer.dao.Identifiable;
import com.finance.fund.transfer.dao.PagedResultDao;
import com.finance.fund.transfer.dao.impl.PagedResultDaoImpl;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Nilesh
 */
class PagedDaoImplTest {

    @Test
    void constructor() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> PagedResultDaoImpl.of(0, 0, null));
        assertEquals("Page number should be positive and starts with 1", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class, () -> PagedResultDaoImpl.of(1, 0, null));
        assertEquals("Amount from records per page should be greater than zero", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class, () -> PagedResultDaoImpl.of(1, 1, null));
        assertEquals("Content cannot be null", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class, () -> PagedResultDaoImpl.of(1, 1, integers()));
        assertEquals("Pageable content size is too big", e.getLocalizedMessage());
    }

    @Test
    void hasMore() {
        PagedResultDao<Integer> result = PagedResultDaoImpl.of(1, 4, integers());
        assertTrue(result.hasMore());
        assertEquals(1, result.getPageNumber());
        assertEquals(4, result.getRecordsPerPage());
        assertEquals(4, result.getContent().size());
        assertTrue(result.getContent().containsAll(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    void noHasMore() {
        PagedResultDao<Integer> result = PagedResultDaoImpl.of(11, 10, integers());
        assertFalse(result.hasMore());
        assertEquals(11, result.getPageNumber());
        assertEquals(10, result.getRecordsPerPage());
        assertEquals(5, result.getContent().size());
        assertTrue(result.getContent().containsAll(Arrays.asList(1, 2, 3, 4, 5)));
    }

    @Test
    void firstPage() {
        PagedResultDao<Identifiable> result = PagedResultDaoImpl.from(1, 2, make());
        assertTrue(result.hasMore());
        assertEquals(1, result.getPageNumber());
        assertEquals(2, result.getRecordsPerPage());
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().containsAll(Arrays.asList(new IdentifiableImpl(1), new IdentifiableImpl(2))));
    }

    @Test
    void middlePage() {
        PagedResultDao<Identifiable> result = PagedResultDaoImpl.from(2, 2, make());
        assertTrue(result.hasMore());
        assertEquals(2, result.getPageNumber());
        assertEquals(2, result.getRecordsPerPage());
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().containsAll(Arrays.asList(new IdentifiableImpl(3), new IdentifiableImpl(4))));
    }

    @Test
    void lastPage() {
        PagedResultDao<Identifiable> result = PagedResultDaoImpl.from(3, 2, make());
        assertFalse(result.hasMore());
        assertEquals(3, result.getPageNumber());
        assertEquals(2, result.getRecordsPerPage());
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().contains(new IdentifiableImpl(5)));
    }

    private static Deque<Integer> integers() {
        return new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
    }

    private static Map<Long, Identifiable> make() {
        final Map<Long, Identifiable> res = new HashMap<>();
        for (long i = 5; i > 0; --i) {
            res.put(i, new IdentifiableImpl(i));
        }
        return res;
    }

    private static class IdentifiableImpl implements Identifiable {

        private final Long item;

        IdentifiableImpl(long i) {
            this.item = i;
        }

        @Override
        public Long getId() {
            return item;
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof IdentifiableImpl)) {
                return false;
            }

            return item.equals(((IdentifiableImpl) obj).item);
        }
    }
}