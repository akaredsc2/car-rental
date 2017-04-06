package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Created by vitaly on 2017-04-06.
 */
public class MysqlNotificationDao implements NotificationDao {
    private static final Logger logger = LogManager.getLogger(MysqlNotificationDao.class.getName());

    private PooledConnection connection;

    MysqlNotificationDao(PooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Notification> findById(long id) {
        return null;
    }

    @Override
    public OptionalLong findIdOfEntity(Notification notification) {
        return null;
    }

    @Override
    public List<Notification> getAll() {
        return null;
    }

    @Override
    public OptionalLong create(Notification notification) {
        connection.initializeTransaction();

        OptionalLong createdId = OptionalLong.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "insert into notification(notification_status, header, content) values (?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, NotificationStatus.NEW.toString().toLowerCase());
            statement.setString(2, notification.getHeader());
            statement.setString(3, notification.getContent());

            statement.executeUpdate();

            connection.commit();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                createdId = OptionalLong.of(resultSet.getLong(1));
            }

            resultSet.close();
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error while creating notification. Rolling back transaction.", e);
        }

        return createdId;
    }

    @Override
    public int update(long id, Notification entity) {
        return 0;
    }

    @Override
    public List<Notification> findNotificationsByUserId(long userId) {
        return null;
    }

    @Override
    public boolean markNotificationAsViewed(long notificationId) {
        return false;
    }
}
