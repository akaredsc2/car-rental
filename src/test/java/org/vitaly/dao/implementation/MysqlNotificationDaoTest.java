package org.vitaly.dao.implementation;

import org.junit.*;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.data.TestData;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;
import org.vitaly.model.user.User;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by vitaly on 2017-04-06.
 */
public class MysqlNotificationDaoTest {
    private static final String NOTIFICATION_CLEAN_UP_QUERY = "delete from notification";
    private static final String USERS_CLEAN_UP_QUERY = "delete from users";

    private static PooledConnection connection;
    private static NotificationDao notificationDao;
    private static long userId;

    private Notification notification1;
    private Notification notification2;

    @BeforeClass
    public static void init() {
        MysqlConnectionPool pool = MysqlConnectionPool.getTestInstance();
        connection = pool.getConnection();

        DaoFactory factory = DaoFactory.getMysqlDaoFactory();
        notificationDao = factory.createNotificationDao(connection);

        UserDao userDao = factory.createUserDao(connection);
        User client1 = TestData.getInstance().getUser("client1");
        userId = userDao.create(client1).orElseThrow(AssertionError::new);
    }

    @Before
    public void setUp() throws Exception {
        notification1 = TestData.getInstance().getNotification("notification1");
        notification2 = TestData.getInstance().getNotification("notification2");
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(NOTIFICATION_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(USERS_CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }

    @Test
    public void findByIdExistingNotificationReturnsNotification() throws Exception {
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);
        Notification foundNotification = notificationDao.findById(notificationId).orElseThrow(AssertionError::new);

        assertEquals(notification1, foundNotification);
    }

    @Test
    public void findByIdNonExistingNotificationReturnsEmptyOptional() throws Exception {
        boolean isPresent = notificationDao.findById(-1).isPresent();

        assertFalse(isPresent);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findIdOfEntityShouldThrowException() throws Exception {
        notificationDao.findIdOfEntity(notification1);
    }

    @Test
    public void getAllOnEmptyTableReturnsEmptyList() throws Exception {
        List<Notification> notifications = notificationDao.getAll();

        assertThat(notifications, empty());
    }

    @Test
    public void getAllReturnsAllNotifications() throws Exception {
        notificationDao.create(notification1);
        notificationDao.create(notification2);

        List<Notification> notifications = notificationDao.getAll();

        assertThat(notifications, allOf(
                iterableWithSize(2),
                hasItems(notification1, notification2)));
    }

    @Test
    public void successfulCreationOfNotificationReturnsId() throws Exception {
        boolean isCreated = notificationDao.create(notification1).isPresent();

        assertTrue(isCreated);
    }

    @Test
    public void createdNotificationStatusIsNew() throws Exception {
        Field statusField = notification1.getClass().getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(notification1, NotificationStatus.VIEWED);

        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);

        Notification createdNotification = notificationDao.findById(notificationId).orElseThrow(AssertionError::new);

        assertThat(createdNotification.getStatus(), equalTo(NotificationStatus.NEW));
    }

    @Test
    public void failedCreationOfNotificationReturnsEmptyOptional() throws Exception {
        Field headerField = notification1.getClass().getDeclaredField("header");
        headerField.setAccessible(true);
        headerField.set(notification1, null);

        boolean isCreated = notificationDao.create(notification1).isPresent();

        assertFalse(isCreated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullNotificationShouldThrowException() throws Exception {
        notificationDao.create(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateShouldThrowException() throws Exception {
        notificationDao.update(1, notification1);
    }

    @Test
    public void findNotificationsByUserIdOfExistingUserReturnsNotificationList() throws Exception {
        long notificationId1 = notificationDao.create(notification1).orElseThrow(AssertionError::new);
        long notificationId2 = notificationDao.create(notification2).orElseThrow(AssertionError::new);

        notificationDao.addNotificationToUser(notificationId1, userId);
        notificationDao.addNotificationToUser(notificationId2, userId);

        List<Notification> notifications = notificationDao.findNotificationsByUserId(userId);

        assertThat(notifications, allOf(
                iterableWithSize(2),
                hasItems(notification1, notification2)));
    }

    @Test
    public void findNotificationsByUserIdOfNonExistingUserReturnsEmptyList() throws Exception {
        List<Notification> notifications = notificationDao.findNotificationsByUserId(1);

        assertThat(notifications, empty());
    }

    @Test
    public void addExistingNotificationToExistingUserReturnsTrue() throws Exception {
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);

        boolean isAddedNotification = notificationDao.addNotificationToUser(notificationId, userId);

        assertTrue(isAddedNotification);
    }

    @Test
    public void addNonExistingNotificationToExistingUserReturnsFalse() throws Exception {
        boolean isAddedNotification = notificationDao.addNotificationToUser(-1, userId);

        assertFalse(isAddedNotification);
    }

    @Test
    public void addExistingNotificationToNonExistingUserReturnsFalse() throws Exception {
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);
        long userId = -1;

        boolean isAddedNotification = notificationDao.addNotificationToUser(notificationId, userId);

        assertFalse(isAddedNotification);
    }

    @Test
    public void addNonExistingNotificationToNonExistingUserReturnsFalse() throws Exception {
        boolean isAddedNotification = notificationDao.addNotificationToUser(-1, -1);

        assertFalse(isAddedNotification);
    }

    @Test
    public void markExistingNotificationAsViewedReturnsTrue() throws Exception {
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);

        boolean isMarkedViewed = notificationDao.markNotificationAsViewed(notificationId);

        assertTrue(isMarkedViewed);
    }

    @Test
    public void markNonExistingNotificationAsViewedReturnsFalse() throws Exception {
        boolean isMarkedViewed = notificationDao.markNotificationAsViewed(-1);

        assertFalse(isMarkedViewed);
    }

    @Test
    public void markingNewNotificationAsViewedMakesNotificationViewed() throws Exception {
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);

        notificationDao.markNotificationAsViewed(notificationId);

        Notification createdNotification = notificationDao.findById(notificationId).orElseThrow(AssertionError::new);

        assertThat(createdNotification.getStatus(), equalTo(NotificationStatus.VIEWED));
    }

    @Test
    public void markingViewedNotificationAsViewedMakesDoesNotChangeNotification() throws Exception {
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);

        notificationDao.markNotificationAsViewed(notificationId);
        Notification viewedNotification = notificationDao.findById(notificationId).orElseThrow(AssertionError::new);
        long viewedNotificationId = viewedNotification.getId();

        notificationDao.markNotificationAsViewed(viewedNotificationId);

        Notification createdNotification =
                notificationDao.findById(viewedNotificationId).orElseThrow(AssertionError::new);

        assertThat(createdNotification.getStatus(), equalTo(NotificationStatus.VIEWED));
    }
}