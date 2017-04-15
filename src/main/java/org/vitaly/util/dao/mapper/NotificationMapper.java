package org.vitaly.util.dao.mapper;

import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.vitaly.util.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-07.
 */
public class NotificationMapper implements Mapper<Notification> {

    @Override
    public Notification map(ResultSet resultSet) throws SQLException {
        LocalDateTime creationDateTime = resultSet.getTimestamp(NOTIFICATION_NOTIFICATION_DATETIME)
                .toLocalDateTime();
        NotificationStatus notificationStatus = NotificationStatus.valueOf(
                resultSet.getString(NOTIFICATION_NOTIFICATION_STATUS).toUpperCase());

        return new Notification.Builder()
                .setId(resultSet.getLong(NOTIFICATION_NOTIFICATION_ID))
                .setCreationDateTime(creationDateTime)
                .setStatus(notificationStatus)
                .setHeader(resultSet.getString(NOTIFICATION_HEADER))
                .setContent(resultSet.getString(NOTIFICATION_CONTENT))
                .build();
    }
}
