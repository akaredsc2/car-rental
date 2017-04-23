package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.notification.Notification;
import org.vitaly.service.impl.dto.NotificationDto;

/**
 * Created by vitaly on 23.04.17.
 */
public class NotificationDtoMapper implements DtoMapper<Notification, NotificationDto> {

    @Override
    public Notification mapDtoToEntity(NotificationDto dto) {
        return new Notification.Builder()
                .setId(dto.getId())
                .setCreationDateTime(dto.getCreationDateTime())
                .setContent(dto.getContent())
                .setHeader(dto.getHeader())
                .setStatus(dto.getStatus())
                .build();
    }

    @Override
    public NotificationDto mapEntityToDto(Notification entity) {
        NotificationDto notificationDto = new NotificationDto();

        notificationDto.setId(entity.getId());
        notificationDto.setCreationDateTime(entity.getCreationDateTime());
        notificationDto.setContent(entity.getContent());
        notificationDto.setHeader(entity.getHeader());
        notificationDto.setStatus(entity.getStatus());

        return notificationDto;
    }
}
