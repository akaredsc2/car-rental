package org.vitaly.service.abstraction;

import org.vitaly.model.notification.Notification;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface NotificationService {
    void sendNotificationToUser(UserDto userDto, NotificationDto notificationDto);

    List<Notification> findNotificationsOfUser(UserDto userDto);

    void markNotificationAsViewed(NotificationDto notificationDto);
}
