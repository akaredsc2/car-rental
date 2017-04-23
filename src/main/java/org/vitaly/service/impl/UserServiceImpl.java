package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public class UserServiceImpl implements UserService {
    private TransactionFactory transactionFactory;
    private DtoMapperFactory dtoMapperFactory;

    UserServiceImpl(TransactionFactory transactionFactory) {
        this(transactionFactory, new DtoMapperFactory());
    }

    public UserServiceImpl(TransactionFactory transactionFactory, DtoMapperFactory dtoMapperFactory) {
        this.transactionFactory = transactionFactory;
        this.dtoMapperFactory = dtoMapperFactory;
    }

    @Override
    public boolean registerNewUser(UserDto userDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            userDto.setRole(UserRole.CLIENT);
            User user = dtoMapperFactory.getUserDtoMapper().mapDtoToEntity(userDto);

            UserDao userDao = transaction.getUserDao();
            boolean isUserCreated = userDao.create(user).isPresent();

            transaction.commit();

            return isUserCreated;
        }
    }

    @Override
    public Optional<UserDto> authenticate(String login, String password) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<User, UserDto> mapper = dtoMapperFactory.getUserDtoMapper();

            UserDao userDao = transaction.getUserDao();
            return userDao.authenticate(login, password)
                    .map(mapper::mapEntityToDto);
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
