package org.vitaly.dao.impl.mysql.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.*;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.*;
import org.vitaly.dao.impl.mysql.factory.MapperFactory;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;

import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-17.
 */
public class MysqlTransaction implements Transaction {
    private static Logger logger = LogManager.getLogger(MysqlTransaction.class.getName());

    private PooledConnection pooledConnection;
    private DaoTemplate daoTemplate;
    private MapperFactory mapperFactory;

    private BillDao billDao;
    private CarModelDao carModelDao;
    private CarDao carDao;
    private LocationDao locationDao;
    private NotificationDao notificationDao;
    private ReservationDao reservationDao;
    private UserDao userDao;

    private boolean autoCommitBeforeTransaction;

    MysqlTransaction(PooledConnection pooledConnection) {
        this(pooledConnection, new MapperFactory(), false);
    }

    private MysqlTransaction(PooledConnection pooledConnection, MapperFactory mapperFactory,
                             boolean autoCommitBeforeTransaction) {
        this.pooledConnection = pooledConnection;
        this.mapperFactory = mapperFactory;
        this.daoTemplate = new DaoTemplate(pooledConnection);
        this.autoCommitBeforeTransaction = autoCommitBeforeTransaction;
    }

    static Transaction createTransaction(PooledConnection pooledConnection) throws SQLException {
        return createTransaction(pooledConnection, new MapperFactory());
    }

    public static Transaction createTransaction(PooledConnection pooledConnection, MapperFactory mapperFactory)
            throws SQLException {
        boolean autoCommitBeforeTransaction = pooledConnection.getAutoCommit();
        pooledConnection.setAutoCommit(false);
        pooledConnection.setInTransaction(true);
        return new MysqlTransaction(pooledConnection, mapperFactory, autoCommitBeforeTransaction);
    }

    @Override
    public BillDao getBillDao() {
        if (billDao == null) {
            billDao = new MysqlBillDao(mapperFactory.getBillMapper(), daoTemplate);
        }
        return billDao;
    }

    @Override
    public CarModelDao getCarModelDao() {
        if (carModelDao == null) {
            carModelDao = new MysqlCarModelDao(mapperFactory.getCarModelMapper(), daoTemplate);
        }
        return carModelDao;
    }

    @Override
    public CarDao getCarDao() {
        if (carDao == null) {
            carDao = new MysqlCarDao(mapperFactory.getCarMapper(), daoTemplate);
        }
        return carDao;
    }

    @Override
    public LocationDao getLocationDao() {
        if (locationDao == null) {
            locationDao = new MysqlLocationDao(mapperFactory.getLocationMapper(), daoTemplate);
        }
        return locationDao;
    }

    @Override
    public NotificationDao getNotificationDao() {
        if (notificationDao == null) {
            notificationDao = new MysqlNotificationDao(mapperFactory.getNotificationMapper(), daoTemplate);
        }
        return notificationDao;
    }

    @Override
    public ReservationDao getReservationDao() {
        if (reservationDao == null) {
            reservationDao = new MysqlReservationDao(mapperFactory.getReservationMapper(), daoTemplate);
        }
        return reservationDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new MysqlUserDao(mapperFactory.getUserMapper(), daoTemplate);
        }
        return userDao;
    }

    @Override
    public void commit() {
        try {
            pooledConnection.commit();
        } catch (SQLException e) {
            String message = "Failed to commit transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    @Override
    public void rollback() {
        try {
            pooledConnection.rollback();
        } catch (SQLException e) {
            String message = "Failed to rollback transaction";
            logger.error(message, e);
            throw new DaoException(message, e);
        }

    }

    @Override
    public void close() {
        try {
            rollback();

            pooledConnection.setAutoCommit(autoCommitBeforeTransaction);
            pooledConnection.setInTransaction(false);
            pooledConnection.close();
        } catch (SQLException e) {
            String message = "Failed to close pooledConnection";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }
}
