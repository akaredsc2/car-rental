package org.vitaly.listener;

import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by vitaly on 2017-04-27.
 */
public class ServletContextListenerImpl implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MysqlConnectionPool.configureConnectionPool(MysqlConnectionPool.CONNECTION_PROPERTIES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
