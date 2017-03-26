package org.vitaly.connectionPool.implementation;

import org.junit.Test;
import org.vitaly.connectionPool.abstraction.ConnectionPool;
import org.vitaly.connectionPool.abstraction.PooledConnection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlConnectionPoolTest {
    private ConnectionPool connectionPool;

    @Test
    public void pooledConnectionRegularPoolIsNotNull() throws Exception {
        connectionPool = MysqlConnectionPool.getInstance();

        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, allOf(
                    notNullValue(),
                    instanceOf(MysqlPooledConnection.class)));
        }
    }

    @Test
    public void pooledConnectionFromTestPoolIsNotNull() throws Exception {
        connectionPool = MysqlConnectionPool.getTestInstance();

        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, allOf(
                    notNullValue(),
                    instanceOf(MysqlPooledConnection.class)));
        }
    }
}