package org.vitaly.dao.abstraction.connectionPool;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by vitaly on 2017-03-25.
 */
public interface PooledConnection extends AutoCloseable {
    void setAutoCommit(boolean autoCommit) throws SQLException;

    boolean getAutoCommit() throws SQLException;

    boolean isInTransaction();

    void setInTransaction(boolean isInTransaction);

    void setTransactionIsolation(int isolationLevel) throws SQLException;

    PreparedStatement prepareStatement(String sql) throws SQLException;

    PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    @Override
    void close() throws SQLException;
}
