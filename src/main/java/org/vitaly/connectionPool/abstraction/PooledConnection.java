package org.vitaly.connectionPool.abstraction;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vitaly on 2017-03-25.
 */
public interface PooledConnection extends AutoCloseable {
    void initializeTransaction();

    Statement prepareStatement(String sql) throws SQLException;

    void commit();

    void rollback();

    @Override
    void close();
}
