package org.vitaly.connectionPool.implementation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vitaly on 2017-03-26.
 */
public class ConnectionPoolBuilderTest {
    private MysqlConnectionPool.Builder builder;

    @Before
    public void setUp() throws Exception {
        builder = new MysqlConnectionPool.Builder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setUrlWithNullShouldThrowException() throws Exception {
        builder.setUrl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setUsernameWithNullShouldThrowException() throws Exception {
        builder.setUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPasswordWithNullShouldThrowException() throws Exception {
        builder.setPassword(null);
    }
}