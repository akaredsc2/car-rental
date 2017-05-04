package org.vitaly.dao.impl.mysql.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MysqlConnectionPool.class})
@PowerMockIgnore("javax.management.*")
public class TransactionManagerTest {

    @Test
    public void getConnectionReturnsSameConnectionEveryTimeWhileInTransaction() throws Exception {
        TransactionManager.startTransaction();

        try (PooledConnection firstConnection = TransactionManager.getConnection();
             PooledConnection secondConnection = TransactionManager.getConnection()) {

            assertThat(firstConnection, sameInstance(secondConnection));

            TransactionManager.commit();
        }
    }

    @Test
    public void connectionInTransactionHasAutocommitSetToFalse() throws Exception {
        TransactionManager.startTransaction();

        try (PooledConnection connection = TransactionManager.getConnection();) {
            assertFalse(connection.getAutoCommit());
        }

        TransactionManager.commit();
    }

    @Test
    public void connectionInTransactionHasIsInTransactionFlagSetToTrue() throws Exception {
        TransactionManager.startTransaction();

        try (PooledConnection connection = TransactionManager.getConnection()) {
            assertTrue(connection.isInTransaction());
        }

        TransactionManager.commit();
    }

    @Test
    public void committingTransactionSetsAutocommitToTrue() throws Exception {
        TransactionManager.startTransaction();
        try (PooledConnection connection = TransactionManager.getConnection()) {
            TransactionManager.commit();

            assertTrue(connection.getAutoCommit());
        }
    }

    @Test
    public void committingTransactionSetsIsInTransactionFlagSetToFalse() throws Exception {
        TransactionManager.startTransaction();
        try (PooledConnection connection = TransactionManager.getConnection()) {
            TransactionManager.commit();

            assertFalse(connection.isInTransaction());
        }
    }

    @Test
    public void committingTransactionCallsCommitOnConnection() throws Exception {
        PooledConnection connection = stab();
        TransactionManager.startTransaction();
        TransactionManager.commit();

        verify(connection).commit();
    }

    @Test
    public void rollingBackTransactionSetsAutocommitToTrue() throws Exception {
        TransactionManager.startTransaction();
        try (PooledConnection connection = TransactionManager.getConnection()) {
            TransactionManager.rollback();

            assertTrue(connection.getAutoCommit());
        }
    }

    @Test
    public void rollingBackTransactionSetsIsInTransactionFlagSetToFalse() throws Exception {
        TransactionManager.startTransaction();
        try (PooledConnection connection = TransactionManager.getConnection()) {
            TransactionManager.rollback();

            assertFalse(connection.isInTransaction());
        }
    }

    @Test
    public void rollingBackTransactionCallsRollbackOnConnection() throws Exception {
        PooledConnection connection = stab();
        TransactionManager.startTransaction();
        TransactionManager.rollback();

        verify(connection).rollback();
    }

    private PooledConnection stab() {
        PowerMockito.mockStatic(MysqlConnectionPool.class);
        MysqlConnectionPool pool = mock(MysqlConnectionPool.class);
        PooledConnection connection = mock(PooledConnection.class);

        PowerMockito.when(MysqlConnectionPool.getInstance()).thenReturn(pool);
        when(pool.getConnection()).thenReturn(connection);
        return connection;
    }
}