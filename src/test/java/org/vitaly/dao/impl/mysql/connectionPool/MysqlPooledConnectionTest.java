package org.vitaly.dao.impl.mysql.connectionPool;

import org.junit.Test;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;

import java.sql.Connection;

import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-05-04.
 */
public class MysqlPooledConnectionTest {

    @Test
    public void callingCloseOnConnectionInTransactionDoesNotCloseConnection() throws Exception {
        Connection connection = mock(Connection.class);
        PooledConnection pooledConnection = new MysqlPooledConnection(connection);

        pooledConnection.setInTransaction(true);

        verify(connection, never()).close();
    }
}