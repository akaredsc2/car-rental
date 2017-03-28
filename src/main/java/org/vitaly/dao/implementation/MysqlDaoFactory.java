package org.vitaly.dao.implementation;

import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.abstraction.UserDao;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-26.
 */
public class MysqlDaoFactory implements DaoFactory {
    private static final String CONNECTION_MUST_NOT_BE_NULL = "Connection must not be null!";

    private static MysqlDaoFactory instance = new MysqlDaoFactory();

    private MysqlDaoFactory() {
    }

    public static MysqlDaoFactory getInstance() {
        return instance;
    }

    @Override
    public LocationDao createLocationDao(PooledConnection connection) {
        requireNotNull(connection, CONNECTION_MUST_NOT_BE_NULL);

        return new MysqlLocationDao(connection);
    }

    @Override
    public CarDao createCarDao(PooledConnection connection) {
        requireNotNull(connection, CONNECTION_MUST_NOT_BE_NULL);

        return new MysqlCarDao(connection);
    }

    @Override
    public UserDao createUserDao(PooledConnection connection) {
        requireNotNull(connection, CONNECTION_MUST_NOT_BE_NULL);

        return new MysqlUserDao(connection);
    }
}
