package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by vitaly on 2017-04-06.
 */
public class MysqlNotificationDaoTest {
    private static final String CLEAN_UP_QUERY = "delete from notification";
    private static MysqlConnectionPool pool;
    private static DaoFactory factory;

    private Notification notification;
    private PooledConnection connection;
    private NotificationDao notificationDao;

    @BeforeClass
    public static void initPoolAndFactory() {
        pool = MysqlConnectionPool.getTestInstance();
        factory = DaoFactory.getMysqlDaoFactory();
    }

    @Before
    public void setUp() throws Exception {
        notification = new Notification.Builder()
                .setCreationDateTime(LocalDateTime.now())
                .setStatus(NotificationStatus.NEW)
                .setHeader("header")
                .setContent("content")
                .build();

        connection = pool.getConnection();
        notificationDao = factory.createNotificationDao(connection);
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }

    @Test
    public void findById() throws Exception {

    }

    @Test
    public void findIdOfEntity() throws Exception {

    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void successfulCreationOfNotificationReturnsId() throws Exception {
        boolean isCreated = notificationDao.create(notification).isPresent();

        assertTrue(isCreated);
    }

    @Test
    public void failedCreationOfNotificationReturnsEmptyOptional() throws Exception {
        Field headerField = notification.getClass().getDeclaredField("header");
        headerField.setAccessible(true);
        headerField.set(notification, null);

        boolean isCreated = notificationDao.create(notification).isPresent();

        assertFalse(isCreated);
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void findNotificationsByUserId() throws Exception {

    }

    @Test
    public void markNotificationAsViewed() throws Exception {

    }
}