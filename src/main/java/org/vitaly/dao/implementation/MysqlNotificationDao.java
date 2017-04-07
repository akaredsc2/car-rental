package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.Mapper;
import org.vitaly.util.dao.mapper.NotificationMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by vitaly on 2017-04-06.
 */
public class MysqlNotificationDao implements NotificationDao {
    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM notification " +
                    "WHERE notification_id = ?";
    private static final String GET_ALL_QUERY =
            "SELECT * " +
                    "FROM notification";
    private static final String FIND_NOTIFICATIONS_BY_USER_ID =
            "SELECT * " +
                    "FROM notification " +
                    "WHERE user_id = ?";
    private static final String CREATE_QUERY =
            "INSERT INTO notification(notification_status, header, content) " +
                    "VALUES (?, ?, ?)";

    private static final Logger logger = LogManager.getLogger(MysqlNotificationDao.class.getName());

    private PooledConnection connection;
    private Mapper<Notification> mapper;
    private DaoTemplate daoTemplate;

    MysqlNotificationDao(PooledConnection connection) {
        this.connection = connection;
        this.mapper = new NotificationMapper();
        this.daoTemplate = new DaoTemplate(connection);
    }

    @Override
    public Optional<Notification> findById(long id) {
        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, id);

        Notification notification = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(notification);
    }

    @Override
    public OptionalLong findIdOfEntity(Notification notification) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error("Error while calling unsupported operation.", e);
        throw e;
    }

    @Override
    public List<Notification> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, new TreeMap<>());
    }

    @Override
    public OptionalLong create(Notification notification) {
        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, NotificationStatus.NEW.toString().toLowerCase());
        parameterMap.put(2, notification.getHeader());
        parameterMap.put(3, notification.getContent());

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return createdId == null ? OptionalLong.empty() : OptionalLong.of(createdId);
    }

    @Override
    public int update(long id, Notification entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error("Error while calling unsupported operation.", e);
        throw e;
    }

    @Override
    public List<Notification> findNotificationsByUserId(long userId) {
        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, userId);

        return daoTemplate.executeSelect(FIND_NOTIFICATIONS_BY_USER_ID, mapper, parameterMap);
    }

    @Override
    public boolean addNotificationToUser(long notificationId, long userId) {
        connection.initializeTransaction();

        try (PreparedStatement statement =
                     connection.prepareStatement("update notification set user_id = ? where notification_id = ?")) {
            statement.setLong(1, userId);
            statement.setLong(2, notificationId);

            statement.executeUpdate();

            connection.commit();

            int updateCount = statement.getUpdateCount();
            return updateCount > 0;
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error while adding notification to user. Rolling back transaction", e);
        }
        return false;
    }

    @Override
    public boolean markNotificationAsViewed(long notificationId) {
        connection.initializeTransaction();

        try (PreparedStatement statement =
                     connection.prepareStatement("update notification set notification_status = ? where notification_id = ?")) {
            statement.setString(1, NotificationStatus.VIEWED.toString().toLowerCase());
            statement.setLong(2, notificationId);

            statement.executeUpdate();

            connection.commit();

            int updateCount = statement.getUpdateCount();
            return updateCount > 0;
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error while changing notification status. Rolling back transaction", e);
        }
        return false;
    }
}
