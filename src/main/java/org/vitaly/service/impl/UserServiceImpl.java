package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public class UserServiceImpl implements UserService {
    private TransactionFactory transactionFactory;

    public UserServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public boolean registerNewUser(UserDto userDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            User user = new User.Builder()
                    .setLogin(userDto.getLogin())
                    .setPassword(userDto.getPassword())
                    .setFullName(userDto.getFullName())
                    .setBirthDate(userDto.getBirthDate())
                    .setPassportNumber(userDto.getPassportNumber())
                    .setDriverLicenceNumber(userDto.getDriverLicenceNumber())
                    .setRole(UserRole.CLIENT)
                    .setReservations(Collections.emptyList())
                    .setNotifications(Collections.emptyList())
                    .build();

            UserDao userDao = transaction.getUserDao();
            boolean isUserCreated = userDao.create(user).isPresent();

            transaction.commit();

            return isUserCreated;
        }
    }

    @Override
    public Optional<User> authenticate(String login, String password) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            UserDao userDao = transaction.getUserDao();
            return userDao.authenticate(login, password);
        }
    }

    @Override
    public void changeRole(UserDto userDto, UserRole newRole) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long userId = userDto.getId();
            UserDao userDao = transaction.getUserDao();
            userDao.changeRole(userId, newRole);

            transaction.commit();
        }
    }

    @Override
    public void changePassword(UserDto userDto, String newPassword) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long userId = userDto.getId();
            UserDao userDao = transaction.getUserDao();
            userDao.changePassword(userId, newPassword);

            transaction.commit();
        }
    }
}
