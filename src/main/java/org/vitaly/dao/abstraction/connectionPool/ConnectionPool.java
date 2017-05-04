package org.vitaly.dao.abstraction.connectionPool;

/**
 * Created by vitaly on 2017-03-25.
 */
public interface ConnectionPool {
    PooledConnection getConnection();
}
