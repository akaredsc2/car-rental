package org.vitaly.util.dao.mapper;

import org.junit.Test;
import org.vitaly.data.TestData;
import org.vitaly.model.notification.Notification;

import java.sql.ResultSet;
import java.sql.Timestamp;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-15.
 */
public class NotificationMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<Notification> mapper = new NotificationMapper();

    @Test
    public void mapCorrectlySetsNotificationParameters() throws Exception {
        Notification expectedNotification = TestData.getInstance().getNotification("notification1");

        when(resultSet.getLong(NOTIFICATION_NOTIFICATION_ID)).thenReturn(expectedNotification.getId());
        when(resultSet.getTimestamp(NOTIFICATION_NOTIFICATION_DATETIME))
                .thenReturn(Timestamp.valueOf(expectedNotification.getCreationDateTime()));
        when(resultSet.getString(NOTIFICATION_NOTIFICATION_STATUS)).thenReturn(
                expectedNotification.getStatus().toString());
        when(resultSet.getString(NOTIFICATION_HEADER)).thenReturn(expectedNotification.getHeader());
        when(resultSet.getString(NOTIFICATION_CONTENT)).thenReturn(expectedNotification.getContent());

        Notification actualNotification = mapper.map(resultSet);

        assertThat(actualNotification, allOf(
                equalTo(expectedNotification),
                hasId(expectedNotification.getId())));
    }
}