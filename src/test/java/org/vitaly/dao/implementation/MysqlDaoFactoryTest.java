package org.vitaly.dao.implementation;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.dao.abstraction.DaoFactory;

import static org.junit.Assert.*;

/**
 * Created by vitaly on 2017-04-06.
 */
public class MysqlDaoFactoryTest {
    private DaoFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = MysqlDaoFactory.getInstance();
    }

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
}