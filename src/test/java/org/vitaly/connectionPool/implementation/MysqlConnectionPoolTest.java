package org.vitaly.connectionPool.implementation;

import org.junit.Test;
import org.vitaly.connectionPool.abstraction.ConnectionPool;
import org.vitaly.connectionPool.abstraction.PooledConnection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlConnectionPoolTest {
    @Test
    public void pooledConnectionRegularPoolIsNotNull() throws Exception {
        ConnectionPool connectionPool = MysqlConnectionPool.getInstance();

        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, instanceOf(MysqlPooledConnection.class));
        }
    }

    @Test
    public void pooledConnectionFromTestPoolIsNotNull() throws Exception {
        ConnectionPool connectionPool = MysqlConnectionPool.getTestInstance();

        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, instanceOf(MysqlPooledConnection.class));
        }
    }
}