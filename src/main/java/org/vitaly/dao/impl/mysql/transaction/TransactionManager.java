package org.vitaly.dao.impl.mysql.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-17.
 */
public class TransactionManager {
    private static Logger logger = LogManager.getLogger(TransactionManager.class.getName());

    private static ThreadLocal<PooledConnection> pooledConnectionThreadLocal = new ThreadLocal<>();

    private TransactionManager() {
    }

    public static PooledConnection getConnection() {
        PooledConnection pooledConnection = pooledConnectionThreadLocal.get();

        if (pooledConnection == null) {
            pooledConnection = MysqlConnectionPool.getInstance().getConnection();
        }

        return pooledConnection;
    }

    public static void startTransaction() {
        startTransactionWithIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

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

    public static boolean endTransaction(boolean commitCondition) {
        if (commitCondition) {
            TransactionManager.commit();
        } else {
            TransactionManager.rollback();
        }

        return commitCondition;
    }
}
