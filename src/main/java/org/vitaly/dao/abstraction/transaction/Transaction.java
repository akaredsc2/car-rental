package org.vitaly.dao.abstraction.transaction;

/**
 * Created by vitaly on 2017-04-17.
 */
public interface Transaction extends AutoCloseable {
    void commit();

    void rollback();

    @Override
    void close();
}
