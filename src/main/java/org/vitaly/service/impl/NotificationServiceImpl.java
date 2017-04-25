package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.Transaction;
import org.vitaly.model.notification.Notification;
import org.vitaly.service.abstraction.NotificationService;
import org.vitaly.service.exception.ServiceException;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotificationToUser(UserDto userDto, NotificationDto notificationDto) {
        try (Transaction transaction = Transaction.startTransaction()) {
            Notification notification = DtoMapperFactory.getInstance()
                    .getNotificationDtoMapper()
                    .mapDtoToEntity(notificationDto);

            NotificationDao notificationDao = MysqlDaoFactory.getInstance().getNotificationDao();

            long createdNotificationId = notificationDao.create(notification).orElseThrow(ServiceException::new);
            long userId = userDto.getId();
            notificationDao.addNotificationToUser(createdNotificationId, userId);

            transaction.commit();
        }
    }

    @Override
    public List<NotificationDto> findNotificationsOfUser(UserDto userDto) {
        DtoMapper<Notification, NotificationDto> mapper = DtoMapperFactory.getInstance().getNotificationDtoMapper();

        long userId = userDto.getId();
        NotificationDao notificationDao = MysqlDaoFactory.getInstance().getNotificationDao();
        return notificationDao.findNotificationsByUserId(userId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markNotificationAsViewed(NotificationDto notificationDto) {
        try (Transaction transaction = Transaction.startTransaction()) {
            long notificationId = notificationDto.getId();
            NotificationDao notificationDao = MysqlDaoFactory.getInstance().getNotificationDao();
            notificationDao.markNotificationAsViewed(notificationId);

            transaction.commit();
        }
    }
}
