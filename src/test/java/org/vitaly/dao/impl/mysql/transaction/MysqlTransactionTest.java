package org.vitaly.dao.impl.mysql.transaction;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.abstraction.transaction.Transaction;

import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-17.
 */
public class MysqlTransactionTest {
    private PooledConnection connection = mock(PooledConnection.class);
    private Transaction transaction = new MysqlTransaction(connection);

    @Test
    public void createTransactionSetsAutoCommitFalse() throws Exception {
        MysqlTransaction.createTransaction(connection);

        verify(connection).setAutoCommit(false);
    }

    @Test
    public void commitCallsConnectionCommit() throws Exception {
        transaction.commit();

        verify(connection).commit();
    }

    @Test
    public void rollbackCallsConnectionRollback() throws Exception {
        transaction.rollback();

        verify(connection).rollback();
    }

    @Test
    public void closeCallsConnectionRollbackAndClose() throws Exception {
        transaction.close();

        InOrder inOrder = inOrder(connection);
        inOrder.verify(connection).rollback();
        inOrder.verify(connection).close();
    }

    @Test
    public void closeResetsAutoCommitToLevelBeforeTransaction() throws Exception {
        when(connection.getAutoCommit()).thenReturn(true);

        MysqlTransaction
                .createTransaction(connection)
                .close();

        InOrder inOrder = Mockito.inOrder(connection);
        inOrder.verify(connection).setAutoCommit(false);
        inOrder.verify(connection).setAutoCommit(true);
    }
}