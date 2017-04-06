package org.vitaly.dao.abstraction;

import org.vitaly.model.notification.Notification;

import java.util.List;

/**
 * Created by vitaly on 2017-04-06.
 */
public interface NotificationDao extends AbstractDao<Notification> {
    List<Notification> findNotificationsByUserId(long userId);

    boolean markNotificationAsViewed(long notificationId);
}
