package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MysqlDaoFactory.class, TransactionManager.class})
@PowerMockIgnore("javax.management.*")
public class UserServiceImplTest {
    private TransactionManager transactionManager = mock(TransactionManager.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private UserDao userDao = mock(UserDao.class);
    private UserService userService = new UserServiceImpl();

    @Test
    public void registerNewUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setLogin("login");
        userDto.setPassword("password");
        userDto.setFullName("fool name");
        userDto.setBirthDate(LocalDate.now());
        userDto.setPassportNumber("passport");
        userDto.setDriverLicenceNumber("driver licence number");
        userDto.setReservationDtoList(Collections.emptyList());
        userDto.setNotificationDtoList(Collections.emptyList());

        stab();
        when(userDao.create(any())).thenReturn(Optional.empty());
        userService.registerNewUser(userDto);

        InOrder inOrder = inOrder(userDao, transactionManager);
        inOrder.verify(userDao).create(any());
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }

    private void stab() {
        PowerMockito.mockStatic(TransactionManager.class);
//        PowerMockito.when(TransactionManager.startTransaction()).thenReturn(transactionManager);
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(daoFactory.getUserDao()).thenReturn(userDao);
    }

    @Test
    public void authenticate() throws Exception {
        String login = "login";
        String password = "password";

        stab();
        when(userDao.authenticate(login, password)).thenReturn(Optional.empty());
        userService.authenticate(login, password);

        verify(userDao).authenticate(login, password);
    }

    @Test
    public void changeRole() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        UserRole role = UserRole.ADMIN;

        stab();
        userService.changeRole(userDto, role);

        InOrder inOrder = inOrder(userDao, transactionManager);
        inOrder.verify(userDao).changeRole(userId, role);
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }

    @Test
    public void changePassword() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        String newPassword = "new password";

        stab();
        userService.changePassword(userDto, newPassword);

        InOrder inOrder = inOrder(userDao, transactionManager);
        inOrder.verify(userDao).changePassword(userId, newPassword);
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }
}