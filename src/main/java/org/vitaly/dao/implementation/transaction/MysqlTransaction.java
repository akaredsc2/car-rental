package org.vitaly.dao.implementation.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.abstraction.*;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.implementation.*;

import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-17.
 */
public class MysqlTransaction implements Transaction {
    private static Logger logger = LogManager.getLogger(MysqlTransaction.class.getName());

    private PooledConnection pooledConnection;
    private boolean autoCommitBeforeTransaction;

    private BillDao billDao;
    private CarDao carDao;
    private LocationDao locationDao;
    private NotificationDao notificationDao;
    private ReservationDao reservationDao;
    private UserDao userDao;

    MysqlTransaction(PooledConnection pooledConnection) {
        this(pooledConnection, false);
    }

    private MysqlTransaction(PooledConnection pooledConnection, boolean autoCommitBeforeTransaction) {
        this.pooledConnection = pooledConnection;
        this.autoCommitBeforeTransaction = autoCommitBeforeTransaction;
    }

    public static Transaction createTransaction(PooledConnection pooledConnection) throws SQLException {
        pooledConnection.setAutoCommit(false);
        return new MysqlTransaction(pooledConnection, pooledConnection.getAutoCommit());
    }

    @Override
    public BillDao getBillDao() {
        if (billDao == null) {
            billDao = new MysqlBillDao(pooledConnection);
        }
        return billDao;
    }

    @Override
    public CarDao getCarDao() {
        if (carDao == null) {
            carDao = new MysqlCarDao(pooledConnection);
        }
        return carDao;
    }

    @Override
    public LocationDao getLocationDao() {
        if (locationDao == null) {
            locationDao = new MysqlLocationDao(pooledConnection);
        }
        return locationDao;
    }

    @Override
    public NotificationDao getNotificationDao() {
        if (notificationDao == null) {
            notificationDao = new MysqlNotificationDao(pooledConnection);
        }
        return notificationDao;
    }

    @Override
    public ReservationDao getReservationDao() {
        if (reservationDao == null) {
            reservationDao = new MysqlReservationDao(pooledConnection);
        }
        return reservationDao;
    }

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new MysqlUserDao(pooledConnection);
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
            pooledConnection.close();
        } catch (SQLException e) {
            String message = "Failed to close pooledConnection";
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }
}
