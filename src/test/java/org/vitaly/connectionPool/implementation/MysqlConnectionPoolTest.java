package org.vitaly.connectionPool.implementation;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
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

    @Before
    public void setUp() throws Exception {
        connectionPool = new MysqlConnectionPool(new BasicDataSource());
    }

    @Test
    public void pooledConnectionIsNotNull() throws Exception {
        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, allOf(
                    notNullValue(),
                    instanceOf(MysqlPooledConnection.class)));
        }
    }
}