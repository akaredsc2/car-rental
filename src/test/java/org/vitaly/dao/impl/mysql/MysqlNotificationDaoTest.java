package org.vitaly.dao.impl.mysql;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.exception.DaoException;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;
import org.vitaly.data.TestData;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;
import org.vitaly.model.user.User;

import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by vitaly on 2017-04-06.
 */
public class MysqlNotificationDaoTest {
    private static final String USERS_CLEAN_UP_QUERY = "delete from users";

    private static PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();
    private static NotificationDao notificationDao = new MysqlNotificationDao(connection);

    private static final long userId;

    static {
        User client1 = TestData.getInstance().getUser("client1");
        UserDao userDao = new MysqlUserDao(connection);
        userId = userDao.create(client1).orElseThrow(AssertionError::new);

        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Notification notification1 = TestData.getInstance().getNotification("notification1");
    private Notification notification2 = TestData.getInstance().getNotification("notification2");

    @After
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
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
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);

        Notification createdNotification = notificationDao.findById(notificationId).orElseThrow(AssertionError::new);

        assertThat(createdNotification.getStatus(), equalTo(NotificationStatus.NEW));
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

    @Test(expected = DaoException.class)
    public void addExistingNotificationToNonExistingUserShouldThrowException() throws Exception {
        long notificationId = notificationDao.create(notification1).orElseThrow(AssertionError::new);

        notificationDao.addNotificationToUser(notificationId, -1);
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