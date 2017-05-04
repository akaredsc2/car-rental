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
        ConnectionPool connectionPool = MysqlConnectionPool.getInstance();

        try (PooledConnection pooledConnection = connectionPool.getConnection()) {
            assertThat(pooledConnection, instanceOf(MysqlPooledConnection.class));
        }
    }
}