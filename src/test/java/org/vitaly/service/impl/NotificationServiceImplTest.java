package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.NotificationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.notification.NotificationStatus;
import org.vitaly.service.abstraction.NotificationService;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MysqlDaoFactory.class, TransactionManager.class})
@PowerMockIgnore("javax.management.*")
public class NotificationServiceImplTest {
    private TransactionManager transactionManager = mock(TransactionManager.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private NotificationDao notificationDao = mock(NotificationDao.class);
    private NotificationService notificationService = new NotificationServiceImpl();

    @Test
    public void sendNotificationToUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(10);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setCreationDateTime(LocalDateTime.now());
        notificationDto.setHeader("header");
        notificationDto.setContent("content");
        notificationDto.setStatus(NotificationStatus.VIEWED);
        long createdNotificationId = 1L;

        stab();
        when(notificationDao.create(any())).thenReturn(Optional.of(createdNotificationId));
        notificationService.sendNotificationToUser(userDto, notificationDto);

        InOrder inOrder = Mockito.inOrder(notificationDao, transactionManager);
        inOrder.verify(notificationDao).create(any());
        inOrder.verify(notificationDao).addNotificationToUser(createdNotificationId, userDto.getId());
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }

    private void stab() {
        PowerMockito.mockStatic(TransactionManager.class);
//        PowerMockito.when(TransactionManager.startTransaction()).thenReturn(transactionManager);
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(daoFactory.getNotificationDao()).thenReturn(notificationDao);
    }

    @Test
    public void findNotificationsOfUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(10);

        stab();
        notificationService.findNotificationsOfUser(userDto);

        verify(notificationDao).findNotificationsByUserId(userDto.getId());
    }

    @Test
    public void markNotificationAsViewed() throws Exception {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(234);

        stab();
        notificationService.markNotificationAsViewed(notificationDto);

        InOrder inOrder = Mockito.inOrder(notificationDao, transactionManager);
        inOrder.verify(notificationDao).markNotificationAsViewed(notificationDto.getId());
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }
}