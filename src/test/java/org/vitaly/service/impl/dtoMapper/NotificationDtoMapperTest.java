package org.vitaly.service.impl.dtoMapper;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 23.04.17.
 */
public class NotificationDtoMapperTest {
    private DtoMapper<Notification, NotificationDto> mapper = DtoMapperFactory.getInstance().getNotificationDtoMapper();
    private NotificationDto expectedNotificationDto;
    private Notification expectedNotification;

    @Before
    public void setUp() throws Exception {
        long id = 6;
        LocalDateTime creationDateTime = LocalDateTime.now();
        NotificationStatus status = NotificationStatus.NEW;
        String header = "header";
        String content = "content";

        expectedNotificationDto = new NotificationDto();
        expectedNotificationDto.setId(id);
        expectedNotificationDto.setCreationDateTime(creationDateTime);
        expectedNotificationDto.setStatus(status);
        expectedNotificationDto.setHeader(header);
        expectedNotificationDto.setContent(content);

        expectedNotification = new Notification.Builder()
                .setId(id)
                .setCreationDateTime(creationDateTime)
                .setStatus(status)
                .setHeader(header)
                .setContent(content)
                .build();
    }

    @Test
    public void mapDtoToEntity() throws Exception {
        Notification actualNotification = mapper.mapDtoToEntity(expectedNotificationDto);

        assertThat(actualNotification, allOf(
                equalTo(expectedNotification),
                hasId(expectedNotification.getId())));
    }

    @Test
    public void mapEntityToDto() throws Exception {
        NotificationDto actualNotificationDto = mapper.mapEntityToDto(expectedNotification);

        assertEquals(expectedNotificationDto, actualNotificationDto);
    }
}