package org.vitaly.dao.implementation.connectionPool;

import org.junit.Test;
import org.vitaly.dao.abstraction.connectionPool.ConnectionPool;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.implementation.connectionPool.MysqlConnectionPool;
import org.vitaly.dao.implementation.connectionPool.MysqlPooledConnection;

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