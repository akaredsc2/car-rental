package org.vitaly.connectionPool.abstraction;

/**
 * Created by vitaly on 2017-03-25.
 */
public interface ConnectionPool {
    PooledConnection getConnection();
}
