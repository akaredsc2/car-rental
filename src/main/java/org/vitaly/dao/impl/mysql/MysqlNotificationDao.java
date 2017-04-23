package org.vitaly.dao.impl.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.mapper.NotificationMapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;

import java.util.*;

import static org.vitaly.util.InputChecker.requireNotNull;

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
            "INSERT INTO notification(notification_datetime, header, content) " +
                    "VALUES (?, ?, ?)";
    private static final String ADD_NOTIFICATION_TO_USER_QUERY =
            "UPDATE notification " +
                    "SET user_id = ? " +
                    "WHERE notification_id = ?";
    private static final String MARK_NOTIFICATION_AS_VIEWED_QUERY =
            "UPDATE notification " +
                    "SET notification_status = ? " +
                    "WHERE notification_id = ?";

    private static final String NOTIFICATION_MUST_NOT_BE_NULL = "Notification must not be null!";

    private static Logger logger = LogManager.getLogger(MysqlNotificationDao.class.getName());

    private Mapper<Notification> mapper;
    private DaoTemplate daoTemplate;

    public MysqlNotificationDao(PooledConnection connection) {
        this(new NotificationMapper(), new DaoTemplate(connection));
    }

    public MysqlNotificationDao(Mapper<Notification> mapper, DaoTemplate daoTemplate) {
        this.mapper = mapper;
        this.daoTemplate = daoTemplate;
    }

    @Override
    public Optional<Notification> findById(long id) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, id);

        Notification notification = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(notification);
    }

    @Override
    public Optional<Long> findIdOfEntity(Notification notification) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error(e);
        throw e;
    }

    @Override
    public List<Notification> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Notification notification) {
        requireNotNull(notification, NOTIFICATION_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, notification.getCreationDateTime());
        parameterMap.put(2, notification.getHeader());
        parameterMap.put(3, notification.getContent());

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Notification entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error(e);
        throw e;

    }

    @Override
    public List<Notification> findNotificationsByUserId(long userId) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, userId);

        return daoTemplate.executeSelect(FIND_NOTIFICATIONS_BY_USER_ID, mapper, parameterMap);
    }

    @Override
    public boolean addNotificationToUser(long notificationId, long userId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, userId);
        parameterMap.put(2, notificationId);

        return daoTemplate.executeUpdate(ADD_NOTIFICATION_TO_USER_QUERY, parameterMap) > 0;
    }

    @Override
    public boolean markNotificationAsViewed(long notificationId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, NotificationStatus.VIEWED.toString().toLowerCase());
        parameterMap.put(2, notificationId);

        int updatedRecordCount = daoTemplate.executeUpdate(MARK_NOTIFICATION_AS_VIEWED_QUERY, parameterMap);
        return updatedRecordCount > 0;
    }
}
