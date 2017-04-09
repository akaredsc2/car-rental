package org.vitaly.data;

import org.vitaly.dao.abstraction.AbstractDao;

/**
 * Created by vitaly on 2017-04-08.
 */
public class TestUtil {
    private TestUtil() {
    }

    public static <T> T createEntityWithId(T entity, AbstractDao<T> dao) {
        long createdId = dao.create(entity).orElseThrow(AssertionError::new);
        return dao.findById(createdId).orElseThrow(AssertionError::new);
    }
}