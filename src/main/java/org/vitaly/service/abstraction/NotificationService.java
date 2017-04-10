package org.vitaly.service.abstraction;

import org.vitaly.model.notification.Notification;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface NotificationService {
    Optional<Notification> findById(long id);

    List<Notification> getAll();

    Optional<Long> create(Notification entity);

    List<Notification> findNotificationsByUserId(long userId);

    boolean addNotificationToUser(long notificationId, long userId);

    boolean markNotificationAsViewed(long notificationId);
}
