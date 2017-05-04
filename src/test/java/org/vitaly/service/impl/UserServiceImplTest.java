package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.model.user.User;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

    private UserService service = new UserServiceImpl();

    @Test
    public void successfulAuthenticationReturnsOptionalWithUserDto() throws Exception {
        String login = "login";
        String password = "password";
        User user = User.createDummyClientWithId(10);

        when(userDao.authenticate(login, password)).thenReturn(Optional.of(user));
        boolean isAuthenticated = service.authenticate(login, password).isPresent();

        assertTrue(isAuthenticated);
    }

    @Test
    public void failedAuthenticationReturnsEmptyOptional() throws Exception {
        String login = "login";
        String password = "password";

        when(userDao.authenticate(login, password)).thenReturn(Optional.empty());
        boolean isAuthenticated = service.authenticate(login, password).isPresent();

        assertFalse(isAuthenticated);
    }

    @Test
    public void successfulRegistrationReturnsTrue() throws Exception {
        UserDto userDto = new UserDto();

        when(userDao.create(any())).thenReturn(Optional.of(5L));
        boolean isRegistered = service.registerNewUser(userDto);

        assertTrue(isRegistered);
    }

    @Test
    public void failedRegistrationReturnsFalse() throws Exception {
        UserDto userDto = new UserDto();

        when(userDao.create(any())).thenReturn(Optional.empty());
        boolean isRegistered = service.registerNewUser(userDto);

        assertFalse(isRegistered);
    }

    @Test
    public void supplyingOldCorrectPasswordWhenChangingPasswordReturnsTrue() throws Exception {
        String login = "login";
        String correctOldPassword = "old password";
        String newPassword = "new password";
        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setPassword(correctOldPassword);
        User user = User.createDummyClientWithId(10);

        when(userDao.authenticate(login, correctOldPassword)).thenReturn(Optional.of(user));
        boolean isPasswordChanged = service.changePassword(userDto, newPassword);

        assertTrue(isPasswordChanged);
    }

    @Test
    public void supplyingIncorrectOldPasswordWhenChangingPasswordReturnsFalse() throws Exception {
        String login = "login";
        String wrongOldPassword = "new password";
        UserDto userDto = new UserDto();
        userDto.setLogin(login);
        userDto.setPassword(wrongOldPassword);

        when(userDao.authenticate(login, wrongOldPassword)).thenReturn(Optional.empty());
        boolean isPasswordChanged = service.changePassword(userDto, wrongOldPassword);

        assertFalse(isPasswordChanged);
    }
}