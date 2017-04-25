package org.vitaly.dao.impl.mysql.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.connectionPool.ConnectionPool;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;

import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MysqlConnectionPool.class)
@PowerMockIgnore("javax.management.*")
public class TransactionTest {
    private MysqlConnectionPool connectionPool = mock(MysqlConnectionPool.class);
    private PooledConnection connection = mock(PooledConnection.class);

    @Test
    public void createTransactionSetsAutoCommitFalse() throws Exception {
        stab();
        Transaction.startTransaction();

        verify(connection).setAutoCommit(false);
    }

    private void stab() {
        PowerMockito.mockStatic(MysqlConnectionPool.class);
        PowerMockito.when(MysqlConnectionPool.getInstance()).thenReturn(connectionPool);
        when(connectionPool.getConnection()).thenReturn(connection);
    }

    @Test
    public void createTransactionSetsIsInTransactionToTrue() throws Exception {
        stab();
        Transaction.startTransaction();

        verify(connection).setInTransaction(true);
    }

    @Test
    public void commitCallsConnectionCommit() throws Exception {
        stab();
        Transaction.startTransaction()
                .commit();

        verify(connection).commit();
    }

    @Test
    public void rollbackCallsConnectionRollback() throws Exception {
        stab();
        Transaction.startTransaction()
                .rollback();

        verify(connection).rollback();
    }

    @Test
    public void closeCallsConnectionRollbackAndClose() throws Exception {
        stab();
        Transaction.startTransaction()
                .close();

        InOrder inOrder = inOrder(connection);
        inOrder.verify(connection).rollback();
        inOrder.verify(connection).close();
    }

    @Test
    public void closeSetsIsInTransactionToFalse() throws Exception {
        stab();
        Transaction.startTransaction()
                .close();

        verify(connection).setInTransaction(false);
    }

    @Test
    public void closeResetsAutoCommitToLevelBeforeTransaction() throws Exception {
        stab();
        when(connection.getAutoCommit()).thenReturn(true);

        Transaction
                .startTransaction()
                .close();

        InOrder inOrder = Mockito.inOrder(connection);
        inOrder.verify(connection).setAutoCommit(false);
        inOrder.verify(connection).setAutoCommit(true);
    }
}