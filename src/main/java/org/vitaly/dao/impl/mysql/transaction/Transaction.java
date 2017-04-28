package org.vitaly.dao.impl.mysql.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;

import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-17.
 */
public class Transaction implements AutoCloseable {
    private static Logger logger = LogManager.getLogger(Transaction.class.getName());

    // TODO: 28.04.17 move from connection pool
//    private static ThreadLocal<MysqlPooledConnection> .....

    private boolean autoCommitBeforeTransaction;

    private Transaction(boolean autoCommitBeforeTransaction) {
        this.autoCommitBeforeTransaction = autoCommitBeforeTransaction;
    }

    public static Transaction startTransaction() {
        PooledConnection pooledConnection = MysqlConnectionPool.getInstance().getConnection();
        try {
            boolean autoCommitBeforeTransaction = pooledConnection.getAutoCommit();
            pooledConnection.setAutoCommit(false);
            pooledConnection.setInTransaction(true);
            return new Transaction(autoCommitBeforeTransaction);
        } catch (SQLException e) {
            String message = "Failed to start transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    public void commit() {
        try {
            MysqlConnectionPool.getInstance()
                    .getConnection()
                    .commit();
        } catch (SQLException e) {
            String message = "Failed to commit transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    public void rollback() {
        try {
            MysqlConnectionPool.getInstance()
                    .getConnection()
                    .rollback();
        } catch (SQLException e) {
            String message = "Failed to rollback transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }

    }

    // TODO: 28.04.17 remove
    @Override
    public void close() {
        try {
            rollback();

            PooledConnection pooledConnection = MysqlConnectionPool.getInstance().getConnection();
            pooledConnection.setAutoCommit(autoCommitBeforeTransaction);
            pooledConnection.setInTransaction(false);
            pooledConnection.close();
        } catch (SQLException e) {
            String message = "Failed to close pooledConnection";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    // TODO: 28.04.17 implement
//    public MysqlPooledConnection getConnection() {
//
//    }
}
