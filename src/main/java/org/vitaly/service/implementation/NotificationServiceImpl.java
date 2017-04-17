package org.vitaly.service.implementation;

import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
//import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.model.notification.Notification;
import org.vitaly.service.abstraction.NotificationService;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public class NotificationServiceImpl implements NotificationService {
    private NotificationDao dao;

    public NotificationServiceImpl(PooledConnection connection) {
//        this.dao = DaoFactory.getMysqlDaoFactory().createNotificationDao(connection);
    }

    @Override
    public Optional<Notification> findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<Notification> getAll() {
        return dao.getAll();
    }

    @Override
    public Optional<Long> create(Notification notification) {
        return dao.create(notification);
    }

    @Override
    public List<Notification> findNotificationsByUserId(long userId) {
        return dao.findNotificationsByUserId(userId);
    }

    @Override
    public boolean addNotificationToUser(long notificationId, long userId) {
        return dao.addNotificationToUser(notificationId, userId);
    }

    @Override
    public boolean markNotificationAsViewed(long notificationId) {
        return dao.markNotificationAsViewed(notificationId);
    }
}
