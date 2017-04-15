package org.vitaly.dao.implementation;

import org.junit.Test;
import org.vitaly.dao.abstraction.DaoFactory;

/**
 * Created by vitaly on 2017-04-06.
 */
public class MysqlDaoFactoryTest {
    private DaoFactory factory = MysqlDaoFactory.getInstance();

    @Test(expected = IllegalArgumentException.class)
    public void createLocationDaoWithNullConnectionShouldThrowException() throws Exception {
        factory.createLocationDao(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCarDaoWithNullConnectionShouldThrowException() throws Exception {
        factory.createCarDao(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUserDaoWithNullConnectionShouldThrowException() throws Exception {
        factory.createUserDao(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNotificationDaoWithNullConnectionShouldThrowException() throws Exception {
        factory.createNotificationDao(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createReservationDaoWithNullConnectionShouldThrowException() throws Exception {
        factory.createReservationDao(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBillDaoWithNullConnectionShouldThrowException() throws Exception {
        factory.createBillDao(null);
    }
}