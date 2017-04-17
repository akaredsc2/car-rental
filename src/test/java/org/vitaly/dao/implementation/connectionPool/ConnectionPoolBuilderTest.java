package org.vitaly.dao.implementation.connectionPool;

import org.junit.Test;

/**
 * Created by vitaly on 2017-03-26.
 */
public class ConnectionPoolBuilderTest {
    private MysqlConnectionPool.Builder builder = new MysqlConnectionPool.Builder();

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