package org.vitaly.dao.impl.mysql.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Transaction manager
 */
public class TransactionManager {
    private static Logger logger = LogManager.getLogger(TransactionManager.class.getName());

    private static ThreadLocal<PooledConnection> pooledConnectionThreadLocal = new ThreadLocal<>();

    private TransactionManager() {
    }

    /**
     * Gets connection. New if has none, old if has one in thread local
     *
     * @return connection wrapper
     */
    public static PooledConnection getConnection() {
        PooledConnection pooledConnection = pooledConnectionThreadLocal.get();

        if (pooledConnection == null) {
            pooledConnection = MysqlConnectionPool.getInstance().getConnection();
        }

        return pooledConnection;
    }

    /**
     * Starts transaction with read committed isolation
     */
    public static void startTransaction() {
        startTransactionWithIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    /**
     * Starts transaction with isolation level
     *
     * @param isolationLevel isolation level
     */
    public static void startTransactionWithIsolation(int isolationLevel) {
        try {
            PooledConnection pooledConnection = MysqlConnectionPool.getInstance().getConnection();
            pooledConnectionThreadLocal.set(pooledConnection);

            pooledConnection.setTransactionIsolation(isolationLevel);
            pooledConnection.setAutoCommit(false);
            pooledConnection.setInTransaction(true);
        } catch (SQLException e) {
            String message = "Failed to start transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    /**
     * Commit transaction
     */
    public static void commit() {
        try {
            PooledConnection pooledConnection = pooledConnectionThreadLocal.get();

            if (pooledConnection == null) {
                throw new DaoException("Can't commit outside of transaction!");
            }

            pooledConnection.commit();
            pooledConnection.setAutoCommit(true);
            pooledConnection.setInTransaction(false);
            pooledConnection.close();
            pooledConnectionThreadLocal.remove();
        } catch (SQLException e) {
            String message = "Failed to commit transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    /**
     * Rollback transaction
     */
    public static void rollback() {
        try {
            PooledConnection pooledConnection = pooledConnectionThreadLocal.get();

            if (pooledConnection == null) {
                throw new DaoException("Can't rollback outside of transaction!");
            }

            pooledConnection.rollback();
            pooledConnection.setAutoCommit(true);
            pooledConnection.setInTransaction(false);
            pooledConnection.close();
            pooledConnectionThreadLocal.remove();
        } catch (SQLException e) {
            String message = "Failed to rollback transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }
}
