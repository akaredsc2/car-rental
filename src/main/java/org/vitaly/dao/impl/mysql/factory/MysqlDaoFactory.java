package org.vitaly.dao.impl.mysql.factory;

import org.vitaly.dao.abstraction.*;
import org.vitaly.dao.impl.mysql.*;

/**
 * Dao factory
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

    /**
     * Instance of factory
     *
     * @return instance of factory
     */
    public static MysqlDaoFactory getInstance() {
        return instance;
    }

    /**
     * Bill dao
     *
     * @return bill dao
     */
    public BillDao getBillDao() {
        return billDao;
    }

    /**
     * Car dao
     *
     * @return car dao
     */
    public CarDao getCarDao() {
        return carDao;
    }

    /**
     * Car model dao
     *
     * @return car model dao
     */
    public CarModelDao getCarModelDao() {
        return carModelDao;
    }

    /**
     * Location dao
     *
     * @return location dao
     */
    public LocationDao getLocationDao() {
        return locationDao;
    }

    /**
     * Reservation dao
     *
     * @return reservation dao
     */
    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    /**
     * User dao
     *
     * @return user dao
     */
    public UserDao getUserDao() {
        return userDao;
    }
}
