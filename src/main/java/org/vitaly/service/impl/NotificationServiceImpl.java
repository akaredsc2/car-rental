package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.notification.Notification;
import org.vitaly.service.abstraction.NotificationService;
import org.vitaly.service.exception.ServiceException;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.NotificationDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class NotificationServiceImpl implements NotificationService {
    private TransactionFactory transactionFactory;

    public NotificationServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void sendNotificationToUser(UserDto userDto, NotificationDto notificationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Notification notification = new NotificationDtoMapper().mapDtoToEntity(notificationDto);

            NotificationDao notificationDao = transaction.getNotificationDao();

            long createdNotificationId = notificationDao.create(notification).orElseThrow(ServiceException::new);
            long userId = userDto.getId();
            notificationDao.addNotificationToUser(createdNotificationId, userId);

            transaction.commit();
        }
    }

    @Override
    public List<NotificationDto> findNotificationsOfUser(UserDto userDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            NotificationDtoMapper mapper = new NotificationDtoMapper();

            long userId = userDto.getId();
            NotificationDao notificationDao = transaction.getNotificationDao();
            return notificationDao.findNotificationsByUserId(userId)
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void markNotificationAsViewed(NotificationDto notificationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long notificationId = notificationDto.getId();
            NotificationDao notificationDao = transaction.getNotificationDao();
            notificationDao.markNotificationAsViewed(notificationId);

            transaction.commit();
        }
    }
}
