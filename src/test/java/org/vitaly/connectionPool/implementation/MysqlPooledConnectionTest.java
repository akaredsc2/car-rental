package org.vitaly.connectionPool.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlPooledConnectionTest {
    private PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();
    private Connection jdbcConnection;
    private Field isTransactionInitializedField;
    private boolean isTransactionInitialized;
    private Field isTransactionEndedField;
    private boolean isTransactionEnded;

    @Before
    public void setUp() throws Exception {
        Field jdbcConnectionField = connection.getClass().getDeclaredField("connection");
        jdbcConnectionField.setAccessible(true);
        jdbcConnection = (Connection) jdbcConnectionField.get(connection);

        isTransactionInitializedField = connection.getClass().getDeclaredField("isTransactionInitialized");
        isTransactionInitializedField.setAccessible(true);

        isTransactionEndedField = connection.getClass().getDeclaredField("isTransactionEnded");
        isTransactionEndedField.setAccessible(true);
    }

    @Test
    public void pooledConnectionHasNotNullJdbcConnection() throws Exception {
        assertThat(jdbcConnection, notNullValue());
    }

    @Test
    public void pooledConnectionHasJdbcConnectionWithAutoCommitSetToTrue() throws Exception {
        assertTrue(jdbcConnection.getAutoCommit());
    }

    @Test
    public void pooledConnectionIsNotInitializedAndIsNotEnded() throws Exception {
        isTransactionInitialized = (boolean) isTransactionInitializedField.get(connection);
        isTransactionEnded = (boolean) isTransactionEndedField.get(connection);
        assertTrue(!isTransactionInitialized && !isTransactionEnded);
    }

    @Test
    public void initializingTransactionSetsAutoCommitToFalse() throws Exception {
        connection.initializeTransaction();

        assertFalse(jdbcConnection.getAutoCommit());
    }

    @Test
    public void initializingTransactionsSetIsInitializedToTrue() throws Exception {
        connection.initializeTransaction();

        isTransactionInitialized = (boolean) isTransactionInitializedField.get(connection);
        assertTrue(isTransactionInitialized);
    }

    @Test
    public void initializingTransactionsSetIsEndedToFalse() throws Exception {
        connection.initializeTransaction();

        isTransactionEnded = (boolean) isTransactionEndedField.get(connection);
        assertFalse(isTransactionEnded);
    }

    @Test
    public void committingSetsAutoCommitToTrue() throws Exception {
        connection.initializeTransaction();
        connection.commit();

        assertTrue(jdbcConnection.getAutoCommit());
    }

    @Test
    public void committingSetsIsEndedToTrue() throws Exception {
        connection.initializeTransaction();
        connection.commit();

        isTransactionEnded = (boolean) isTransactionEndedField.get(connection);
        assertTrue(isTransactionEnded);
    }

    @Test
    public void committingDoesNotAffectIsInitialized() throws Exception {
        connection.initializeTransaction();
        boolean isTransactionInitializedBeforeCommit = (boolean) isTransactionInitializedField.get(connection);
        connection.commit();

        boolean isTransactionInitializedAfterCommit = (boolean) isTransactionInitializedField.get(connection);
        assertThat(isTransactionInitializedBeforeCommit, equalTo(isTransactionInitializedAfterCommit));
    }

    @Test
    public void rollingBackSetsAutoCommitToTrue() throws Exception {
        connection.initializeTransaction();
        connection.rollback();

        assertTrue(jdbcConnection.getAutoCommit());
    }

    @Test
    public void rollingBackSetsIsEndedToTrue() throws Exception {
        connection.initializeTransaction();
        connection.rollback();

        isTransactionEnded = (boolean) isTransactionEndedField.get(connection);
        assertTrue(isTransactionEnded);
    }

    @Test
    public void rollingBackDoesNotAffectIsInitialized() throws Exception {
        connection.initializeTransaction();
        boolean isTransactionInitializedBeforeRollback = (boolean) isTransactionInitializedField.get(connection);
        connection.rollback();

        boolean isTransactionInitializedAfterRollback = (boolean) isTransactionInitializedField.get(connection);
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