package org.vitaly.service.impl;

import org.junit.Test;
import org.mockito.InOrder;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
public class UserServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
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

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getUserDao()).thenReturn(userDao);
        userService.registerNewUser(userDto);

        InOrder inOrder = inOrder(userDao, transaction);
        inOrder.verify(userDao).create(any());
        inOrder.verify(transaction).commit();
    }

    @Test
    public void authenticate() throws Exception {
        String login = "login";
        String password = "password";

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getUserDao()).thenReturn(userDao);
        userService.authenticate(login, password);

        verify(userDao).authenticate(login, password);
    }

    @Test
    public void changeRole() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        UserRole role = UserRole.ADMIN;

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getUserDao()).thenReturn(userDao);
        userService.changeRole(userDto, role);

        InOrder inOrder = inOrder(userDao, transaction);
        inOrder.verify(userDao).changeRole(userId, role);
        inOrder.verify(transaction).commit();
    }

    @Test
    public void changePassword() throws Exception {
        int userId = 1;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        String newPassword = "new password";

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getUserDao()).thenReturn(userDao);
        userService.changePassword(userDto, newPassword);

        InOrder inOrder = inOrder(userDao, transaction);
        inOrder.verify(userDao).changePassword(userId, newPassword);
        inOrder.verify(transaction).commit();
    }
}