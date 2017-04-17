package org.vitaly.dao.impl.mysql.connectionPool;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.ConnectionPool;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;

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
    private static final String USE_SSL_FALSE = "?useSSL=false";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_DEFAULT_AUTO_COMMIT = "db.defaultAutoCommit";
    private static final String DB_DEFAULT_TRANSACTION_ISOLATION = "db.defaultTransactionIsolation";

    private static Logger logger = LogManager.getLogger(MysqlPooledConnection.class.getName());

    private static MysqlConnectionPool instance;
    private static MysqlConnectionPool testInstance;

    private BasicDataSource basicDataSource;

    static {
        try {
            instance = createConnectionPoolFromProperties(CONNECTION_PROPERTIES);
            testInstance = createConnectionPoolFromProperties(TEST_CONNECTION_PROPERTIES);
        } catch (ClassNotFoundException e) {
            logger.fatal("Fatal error while loading driver class", e);
        } catch (IOException e) {
            logger.fatal("Fatal error while initializing connection pool.", e);
        }
    }

    private MysqlConnectionPool(Builder builder) {
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(createUrl(builder));
        basicDataSource.setUsername(builder.username);
        basicDataSource.setPassword(builder.password);
        basicDataSource.setMaxTotal(builder.maxTotal);
        basicDataSource.setDefaultAutoCommit(builder.defaultAutoCommit);
        basicDataSource.setDefaultTransactionIsolation(builder.defaultTransactionIsolation);
    }

    private static MysqlConnectionPool createConnectionPoolFromProperties(String fileName)
            throws IOException, ClassNotFoundException {
        InputStream input = MysqlConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(input);

        String driver = properties.getProperty(DB_DRIVER);
        Class.forName(driver);

        String url = properties.getProperty(DB_URL);
        String user = properties.getProperty(DB_USER);
        String pass = properties.getProperty(DB_PASS);
        boolean useSsl = Boolean.parseBoolean(properties.getProperty(DB_USE_SSL));
        int maxTotal = Integer.parseInt(properties.getProperty(DB_MAX_TOTAL));
        boolean defaultAutoCommit = Boolean.parseBoolean(properties.getProperty(DB_DEFAULT_AUTO_COMMIT));
        int defaultTransactionIsolation = Integer.parseInt(properties.getProperty(DB_DEFAULT_TRANSACTION_ISOLATION));

        return new Builder()
                .setUrl(url)
                .setUsername(user)
                .setPassword(pass)
                .setUseSsl(useSsl)
                .setMaxTotal(maxTotal)
                .setDefaultAutoCommit(defaultAutoCommit)
                .setDefaultTransactionIsolation(defaultTransactionIsolation)
                .build();
    }

    private String createUrl(Builder builder) {
        String url = builder.url;
        if (!builder.useSsl) {
            url += USE_SSL_FALSE;
        }
        return url;
    }

    public static MysqlConnectionPool getInstance() {
        return instance;
    }

    public static MysqlConnectionPool getTestInstance() {
        return testInstance;
    }

    @Override
    public PooledConnection getConnection() {
        try {
            Connection connection = basicDataSource.getConnection();
            return new MysqlPooledConnection(connection);
        } catch (SQLException e) {
            String message = "Error while getting connection from pool.";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    static class Builder {
        private String url;
        private String username;
        private String password;
        private int maxTotal;
        private boolean useSsl;
        private boolean defaultAutoCommit;
        private int defaultTransactionIsolation;

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

        public Builder setDefaultAutoCommit(boolean defaultAutoCommit) {
            this.defaultAutoCommit = defaultAutoCommit;
            return this;
        }

        public Builder setDefaultTransactionIsolation(int defaultTransactionIsolation) {
            this.defaultTransactionIsolation = defaultTransactionIsolation;
            return this;
        }

        public MysqlConnectionPool build() {
            return new MysqlConnectionPool(this);
        }
    }
}
