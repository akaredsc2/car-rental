package org.vitaly.dao.impl.mysql.connectionPool;

import org.junit.Test;
import org.vitaly.dao.abstraction.connectionPool.ConnectionPool;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlConnectionPoolTest {
    @Test
    public void pooledConnectionRegularPoolIsNotNull() throws Exception {
        MysqlConnectionPool.configureConnectionPool(MysqlConnectionPool.CONNECTION_PROPERTIES);
        ConnectionPool connectionPool = MysqlConnectionPool.getInstance();

        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, instanceOf(MysqlPooledConnection.class));
        }
    }

    @Test
    public void pooledConnectionFromTestPoolIsNotNull() throws Exception {
        MysqlConnectionPool.configureConnectionPool(MysqlConnectionPool.TEST_CONNECTION_PROPERTIES);
        ConnectionPool connectionPool = MysqlConnectionPool.getInstance();

        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, instanceOf(MysqlPooledConnection.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void configuringWithNullFileNameShouldThrowException() throws Exception {
        MysqlConnectionPool.configureConnectionPool(null);
    }
}