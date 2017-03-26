package org.vitaly.dao.implementation;

import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.util.InputChecker;

/**
 * Created by vitaly on 2017-03-26.
 */
public class MysqlDaoFactory implements DaoFactory {
    private static MysqlDaoFactory instance = new MysqlDaoFactory();

    private MysqlDaoFactory() {
    }

    public static MysqlDaoFactory getInstance() {
        return instance;
    }

    @Override
    public LocationDao createLocationDao(PooledConnection connection) {
        InputChecker.requireNotNull(connection, "Connection must not be null!");

        return new MysqlLocationDao(connection);
    }
}
