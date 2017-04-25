package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
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
@PrepareForTest(MysqlDaoFactory.class)
@PowerMockIgnore("javax.management.*")
public class UserServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private UserDao userDao = mock(UserDao.class);
    private UserService userService = new UserServiceImpl(transactionFactory);

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

        InOrder inOrder = inOrder(userDao, transaction);
        inOrder.verify(userDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    private void stab() {
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(transactionFactory.createTransaction()).thenReturn(transaction);
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

        InOrder inOrder = inOrder(userDao, transaction);
        inOrder.verify(userDao).changeRole(userId, role);
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void changePassword() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        String newPassword = "new password";

        stab();
        userService.changePassword(userDto, newPassword);

        InOrder inOrder = inOrder(userDao, transaction);
        inOrder.verify(userDao).changePassword(userId, newPassword);
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}