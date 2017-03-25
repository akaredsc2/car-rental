package org.vitaly.connectionPool.implementation;

import org.vitaly.connectionPool.abstraction.PooledConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlPooledConnection implements PooledConnection {
    private Connection connection;
    private boolean isTransactionInitialized;
    private boolean isTransactionEnded;

    public MysqlPooledConnection(Connection connection) {
        this.connection = connection;
        this.isTransactionInitialized = false;
        this.isTransactionEnded = false;
    }

    @Override
    public void initializeTransaction() {
        try {
            connection.setAutoCommit(false);
            isTransactionInitialized = true;
            isTransactionEnded = false;
        } catch (SQLException e) {

            // TODO: 2017-03-25 log
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void commit() {
        try {
            connection.commit();
            isTransactionEnded = true;
            connection.setAutoCommit(true);
        } catch (SQLException e) {

            // TODO: 2017-03-25 log
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
            isTransactionEnded = true;
            connection.setAutoCommit(true);
        } catch (SQLException e) {

            // TODO: 2017-03-25 log
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (isTransactionInitialized && !isTransactionEnded) {
                rollback();
            }
            connection.close();
        } catch (SQLException e) {

            // TODO: 2017-03-25 log
            throw new IllegalStateException(e);
        }
    }
}
