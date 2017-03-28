package org.vitaly.connectionPool.implementation;

import org.apache.commons.dbcp2.BasicDataSource;
import org.vitaly.connectionPool.abstraction.ConnectionPool;
import org.vitaly.connectionPool.abstraction.PooledConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-25.
 */
public class MysqlConnectionPool implements ConnectionPool {
    private static final String PATH_SEPARATOR = System.getProperty("file.separator");
    private static final String CONNECTION_PROPERTIES = "db" + PATH_SEPARATOR + "connection.properties";
    private static final String TEST_CONNECTION_PROPERTIES = "db" + PATH_SEPARATOR + "test_connection.properties";
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASS = "db.pass";
    private static final String DB_USE_SSL = "db.useSsl";
    private static final String DB_MAX_TOTAL = "db.maxTotal";
    public static final String USE_SSL_FALSE = "?useSSL=false";

    private static MysqlConnectionPool INSTANCE;
    private static MysqlConnectionPool TEST_INSTANCE;

    static {
        try {
            INSTANCE = createConnectionPoolFromProperties(CONNECTION_PROPERTIES);
            TEST_INSTANCE = createConnectionPoolFromProperties(TEST_CONNECTION_PROPERTIES);
        } catch (IOException e) {

            // TODO: 2017-03-26 log
            throw new IllegalStateException();
        }
    }

    private static MysqlConnectionPool createConnectionPoolFromProperties(String fileName) throws IOException {
        InputStream input = MysqlConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(input);

        String url = properties.getProperty(DB_URL);
        String user = properties.getProperty(DB_USER);
        String pass = properties.getProperty(DB_PASS);
        boolean useSsl = Boolean.valueOf(properties.getProperty(DB_USE_SSL));
        int maxTotal = Integer.valueOf(properties.getProperty(DB_MAX_TOTAL));

        return new Builder()
                .setUrl(url)
                .setUsername(user)
                .setPassword(pass)
                .setUseSsl(useSsl)
                .setMaxTotal(maxTotal)
                .build();
    }

    private BasicDataSource basicDataSource;

    private MysqlConnectionPool(Builder builder) {
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(createUrl(builder));
        basicDataSource.setUsername(builder.username);
        basicDataSource.setPassword(builder.password);
        basicDataSource.setMaxTotal(builder.maxTotal);
    }

    private String createUrl(Builder builder) {
        String url = builder.url;
        if (!builder.useSsl) {
            url += USE_SSL_FALSE;
        }
        return url;
    }

    public static MysqlConnectionPool getInstance() {
        return INSTANCE;
    }

    public static MysqlConnectionPool getTestInstance() {
        return TEST_INSTANCE;
    }

    public PooledConnection getConnection() {
        Connection jdbcConnection;

        try {
            jdbcConnection = basicDataSource.getConnection();
        } catch (SQLException e) {

            // TODO: 2017-03-26 log
            throw new IllegalStateException();
        }

        return new MysqlPooledConnection(jdbcConnection);
    }

    static class Builder {
        private String url;
        private String username;
        private String password;
        private int maxTotal;
        private boolean useSsl;

        public Builder() {
        }

        public Builder setUrl(String url) {
            requireNotNull(url, "Url must not be null!");

            this.url = url;
            return this;
        }

        public Builder setUsername(String username) {
            requireNotNull(url, "Username must not be null!");

            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            requireNotNull(url, "Password must not be null!");

            this.password = password;
            return this;
        }

        public Builder setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }

        public Builder setUseSsl(boolean useSsl) {
            this.useSsl = useSsl;
            return this;
        }

        public MysqlConnectionPool build() {
            return new MysqlConnectionPool(this);
        }
    }
}
