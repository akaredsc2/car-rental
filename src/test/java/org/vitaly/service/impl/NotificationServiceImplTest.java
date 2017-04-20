package org.vitaly.service.impl;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.service.abstraction.NotificationService;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
public class NotificationServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private NotificationDao notificationDao = mock(NotificationDao.class);
    private NotificationService notificationService = new NotificationServiceImpl(transactionFactory);

    @Test
    public void sendNotificationToUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(10);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setCreationDateTime(LocalDateTime.now());
        notificationDto.setHeader("header");
        notificationDto.setContent("content");

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getNotificationDao()).thenReturn(notificationDao);
        long createdNotificationId = 1L;
        when(notificationDao.create(any())).thenReturn(Optional.of(createdNotificationId));
        notificationService.sendNotificationToUser(userDto, notificationDto);

        InOrder inOrder = Mockito.inOrder(notificationDao, transaction);
        inOrder.verify(notificationDao).create(any());
        inOrder.verify(notificationDao).addNotificationToUser(createdNotificationId, userDto.getId());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void findNotificationsOfUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(10);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getNotificationDao()).thenReturn(notificationDao);
        notificationService.findNotificationsOfUser(userDto);

        InOrder inOrder = inOrder(notificationDao, transaction);
        inOrder.verify(notificationDao).findNotificationsByUserId(userDto.getId());
        inOrder.verify(transaction).close();
    }

    @Test
    public void markNotificationAsViewed() throws Exception {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(234);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getNotificationDao()).thenReturn(notificationDao);
        notificationService.markNotificationAsViewed(notificationDto);

        InOrder inOrder = Mockito.inOrder(notificationDao, transaction);
        inOrder.verify(notificationDao).markNotificationAsViewed(notificationDto.getId());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}