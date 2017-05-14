package org.vitaly.dao.abstraction.connectionPool;

/**
 * Wrapper for DataSource
 */
public interface ConnectionPool {

    /**
     * Returns wrapped connection from pool
     *
     * @return wrapped connection from pool
     */
    PooledConnection getConnection();
}
