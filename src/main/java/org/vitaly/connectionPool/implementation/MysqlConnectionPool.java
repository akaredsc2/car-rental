package org.vitaly.connectionPool.implementation;

import org.apache.commons.dbcp2.BasicDataSource;
import org.vitaly.connectionPool.abstraction.ConnectionPool;
import org.vitaly.connectionPool.abstraction.PooledConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlConnectionPool implements ConnectionPool {
    private BasicDataSource basicDataSource;

    public MysqlConnectionPool(BasicDataSource basicDataSource) {
        this.basicDataSource = basicDataSource;
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/carrental");
        basicDataSource.setUsername("vitaly");
        basicDataSource.setPassword("sh2r2p0v");
        basicDataSource.setMaxTotal(21);
    }

    public PooledConnection getConnection() {
        Connection jdbcConnection;

        try {
            jdbcConnection = basicDataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException();
        }

        return new MysqlPooledConnection(jdbcConnection);
    }
}
