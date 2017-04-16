package org.vitaly.connectionPool.implementation;

import org.junit.After;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;

import java.sql.Statement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlPooledConnectionTest {
    private PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();

    @Test
    public void pooledConnectionIsNotInitializedAndIsNotEnded() throws Exception {
        assertTrue(!connection.isTransactionInitialized() && !connection.isTransactionEnded());
    }

    @Test
    public void initializingTransactionSetsAutoCommitToFalse() throws Exception {
        connection.initializeTransaction();

        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void initializingTransactionsSetIsInitializedToTrue() throws Exception {
        connection.initializeTransaction();

        assertTrue(connection.isTransactionInitialized());
    }

    @Test
    public void initializingTransactionsSetIsEndedToFalse() throws Exception {
        connection.initializeTransaction();

        assertFalse(connection.isTransactionEnded());
    }

    @Test
    public void committingSetsAutoCommitToTrue() throws Exception {
        connection.initializeTransaction();
        connection.commit();

        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void committingSetsIsEndedToTrue() throws Exception {
        connection.initializeTransaction();
        connection.commit();

        assertTrue(connection.isTransactionEnded());
    }

    @Test
    public void committingDoesNotAffectIsInitialized() throws Exception {
        connection.initializeTransaction();
        boolean isTransactionInitializedBeforeCommit = connection.isTransactionInitialized();
        connection.commit();

        boolean isTransactionInitializedAfterCommit = connection.isTransactionInitialized();
        assertEquals(isTransactionInitializedBeforeCommit, isTransactionInitializedAfterCommit);
    }

    @Test
    public void rollingBackSetsAutoCommitToTrue() throws Exception {
        connection.initializeTransaction();
        connection.rollback();

        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void rollingBackSetsIsEndedToTrue() throws Exception {
        connection.initializeTransaction();
        connection.rollback();

        assertTrue(connection.isTransactionEnded());
    }

    @Test
    public void rollingBackDoesNotAffectIsInitialized() throws Exception {
        connection.initializeTransaction();
        boolean isTransactionInitializedBeforeRollback = connection.isTransactionInitialized();
        connection.rollback();

        boolean isTransactionInitializedAfterRollback = connection.isTransactionInitialized();
        assertThat(isTransactionInitializedBeforeRollback, equalTo(isTransactionInitializedAfterRollback));
    }

    @Test
    public void preparedStatementIsNotNull() throws Exception {
        String query = "select * from users";
        Statement prepareStatement = connection.prepareStatement(query);

        assertNotNull(prepareStatement);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }
}