package org.vitaly.dao.impl.mysql.factory;

import org.vitaly.dao.abstraction.*;
import org.vitaly.dao.impl.mysql.*;

/**
 * Created by vitaly on 25.04.17.
 */
public class MysqlDaoFactory {
    private static MysqlDaoFactory instance = new MysqlDaoFactory();

    private BillDao billDao;
    private CarDao carDao;
    private CarModelDao carModelDao;
    private LocationDao locationDao;
    private NotificationDao notificationDao;
    private ReservationDao reservationDao;
    private UserDao userDao;

    private MysqlDaoFactory() {
        billDao = new MysqlBillDao();
        carDao = new MysqlCarDao();
        carModelDao = new MysqlCarModelDao();
        locationDao = new MysqlLocationDao();
        notificationDao = new MysqlNotificationDao();
        reservationDao = new MysqlReservationDao();
        userDao = new MysqlUserDao();
    }

    public static MysqlDaoFactory getInstance() {
        return instance;
    }

    public BillDao getBillDao() {
        return billDao;
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public CarModelDao getCarModelDao() {
        return carModelDao;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public NotificationDao getNotificationDao() {
        return notificationDao;
    }

    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
