package org.vitaly.connectionPool.abstraction;

/**
 * Created by vitaly on 2017-03-25.
 */
public interface PooledConnection extends AutoCloseable {
    void initializeTransaction();

    void commit();

    void rollback();

    @Override
    void close();
}
