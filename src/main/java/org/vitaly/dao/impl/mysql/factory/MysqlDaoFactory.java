package org.vitaly.dao.impl.mysql.factory;

import org.vitaly.dao.abstraction.*;
import org.vitaly.dao.impl.mysql.*;

/**
 * Created by vitaly on 25.04.17.
 */
public class MysqlDaoFactory {
    private static MysqlDaoFactory instance = new MysqlDaoFactory();

    private BillDao billDao = new MysqlBillDao();
    private CarDao carDao = new MysqlCarDao();
    private CarModelDao carModelDao = new MysqlCarModelDao();
    private LocationDao locationDao = new MysqlLocationDao();
    private ReservationDao reservationDao = new MysqlReservationDao();
    private UserDao userDao = new MysqlUserDao();

    private MysqlDaoFactory() {
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

    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
