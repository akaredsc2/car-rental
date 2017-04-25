package org.vitaly.dao.impl.mysql.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.exception.DaoException;

import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-17.
 */
public class MysqlTransaction implements Transaction {
    private static Logger logger = LogManager.getLogger(MysqlTransaction.class.getName());

    private PooledConnection pooledConnection;
    private boolean autoCommitBeforeTransaction;

    MysqlTransaction(PooledConnection pooledConnection) {
        this(pooledConnection, false);
    }

    private MysqlTransaction(PooledConnection pooledConnection, boolean autoCommitBeforeTransaction) {
        this.pooledConnection = pooledConnection;
        this.autoCommitBeforeTransaction = autoCommitBeforeTransaction;
    }

    public static Transaction createTransaction(PooledConnection pooledConnection)
            throws SQLException {
        boolean autoCommitBeforeTransaction = pooledConnection.getAutoCommit();
        pooledConnection.setAutoCommit(false);
        pooledConnection.setInTransaction(true);
        return new MysqlTransaction(pooledConnection, autoCommitBeforeTransaction);
    }

    @Override
    public void commit() {
        try {
            pooledConnection.commit();
        } catch (SQLException e) {
            String message = "Failed to commit transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    @Override
    public void rollback() {
        try {
            pooledConnection.rollback();
        } catch (SQLException e) {
            String message = "Failed to rollback transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }

    }

    @Override
    public void close() {
        try {
            rollback();

            pooledConnection.setAutoCommit(autoCommitBeforeTransaction);
            pooledConnection.setInTransaction(false);
            pooledConnection.close();
        } catch (SQLException e) {
            String message = "Failed to close pooledConnection";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }
}
