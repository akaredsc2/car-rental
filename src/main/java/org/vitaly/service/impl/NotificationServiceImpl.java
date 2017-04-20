package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.notification.Notification;
import org.vitaly.service.abstraction.NotificationService;
import org.vitaly.service.exception.ServiceException;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

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
            Notification notification = new Notification.Builder()
                    .setCreationDateTime(LocalDateTime.now())
                    .setContent(notificationDto.getContent())
                    .setHeader(notificationDto.getHeader())
                    .build();
            NotificationDao notificationDao = transaction.getNotificationDao();

            long createdNotificationId = notificationDao.create(notification).orElseThrow(ServiceException::new);
            long userId = userDto.getId();
            notificationDao.addNotificationToUser(createdNotificationId, userId);

            transaction.commit();
        }
    }

    @Override
    public List<Notification> findNotificationsOfUser(UserDto userDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long userId = userDto.getId();
            NotificationDao notificationDao = transaction.getNotificationDao();
            return notificationDao.findNotificationsByUserId(userId);
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
