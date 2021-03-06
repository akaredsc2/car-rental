package org.vitaly.dao.impl.mysql.connectionPool;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.ConnectionPool;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.util.PropertyUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Connection pool for MySQL database
 */
public class MysqlConnectionPool implements ConnectionPool {
    private static final String CONNECTION_PROPERTIES = "db" + File.separator + "connection.properties";

    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASS = "db.pass";
    private static final String DB_USE_SSL = "db.useSsl";
    private static final String DB_MAX_TOTAL = "db.maxTotal";
    private static final String USE_SSL_FALSE = "?useSSL=false";
    private static final String DB_DRIVER = "db.driver";

    private static Logger logger = LogManager.getLogger(MysqlPooledConnection.class.getName());

    private static MysqlConnectionPool instance = new MysqlConnectionPool();

    private DataSource basicDataSource;

    private MysqlConnectionPool() {
        this.basicDataSource = createConfiguredDataSource(CONNECTION_PROPERTIES);
    }

    private static DataSource createConfiguredDataSource(String fileName) {
        try {
            Properties properties = PropertyUtils.readProperties(fileName);

            String driver = properties.getProperty(DB_DRIVER);
            Class.forName(driver);

            String url = createUrl(properties);
            String user = properties.getProperty(DB_USER);
            String pass = properties.getProperty(DB_PASS);
            int maxTotal = Integer.parseInt(properties.getProperty(DB_MAX_TOTAL));

            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(user);
            basicDataSource.setPassword(pass);
            basicDataSource.setMaxTotal(maxTotal);

            return basicDataSource;
        } catch (UncheckedIOException e) {
            logger.fatal("Fatal error while reading initialization properties. Shutting down application.", e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            logger.fatal("Fatal error while loading driver class. Shutting down application.", e);
            System.exit(1);
        }

        return null;
    }

    private static String createUrl(Properties properties) {
        String url = properties.getProperty(DB_URL);
        boolean useSsl = Boolean.parseBoolean(properties.getProperty(DB_USE_SSL));
        if (!useSsl) {
            url += USE_SSL_FALSE;
        }
        return url;
    }

    /**
     * Instance of connection pool
     *
     * @return instance of connection pool
     */
    public static MysqlConnectionPool getInstance() {
        return instance;
    }

    /**
     * @inheritDoc
     */
    @Override
    public synchronized PooledConnection getConnection() {
        try {
            Connection connection = basicDataSource.getConnection();
            return new MysqlPooledConnection(connection);
        } catch (SQLException e) {
            String message = "Error while getting connection from pool.";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }
}
