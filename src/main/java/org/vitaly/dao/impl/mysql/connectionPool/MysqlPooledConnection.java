package org.vitaly.dao.impl.mysql.connectionPool;

import org.vitaly.dao.abstraction.connectionPool.PooledConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Connection wrapper for MySQL connection
 */
public class MysqlPooledConnection implements PooledConnection {
    private Connection connection;
    private boolean isInTransaction;

    MysqlPooledConnection(Connection connection) {
        this.connection = connection;
        this.isInTransaction = false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isInTransaction() {
        return isInTransaction;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setTransactionIsolation(int isolationLevel) throws SQLException {
        connection.setTransactionIsolation(isolationLevel);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setInTransaction(boolean isInTransaction) {
        this.isInTransaction = isInTransaction;
    }

    /**
     * @inheritDoc
     */
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * @inheritDoc
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void close() throws SQLException {
        if (!isInTransaction) {
            connection.close();
        }
    }
}
