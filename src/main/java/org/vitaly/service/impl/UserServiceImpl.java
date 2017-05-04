package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class UserServiceImpl implements UserService {

    @Override
    public boolean registerNewUser(UserDto userDto) {
        TransactionManager.startTransaction();

        userDto.setRole(UserRole.CLIENT);
        User user = DtoMapperFactory.getInstance()
                .getUserDtoMapper()
                .mapDtoToEntity(userDto);

        UserDao userDao = MysqlDaoFactory.getInstance().getUserDao();
        boolean isUserCreated = userDao.create(user).isPresent();

        TransactionManager.commit();

        return isUserCreated;
    }

    @Override
    public Optional<UserDto> authenticate(String login, String password) {
        DtoMapper<User, UserDto> mapper = DtoMapperFactory.getInstance().getUserDtoMapper();

        UserDao userDao = MysqlDaoFactory.getInstance().getUserDao();
        return userDao.authenticate(login, password)
                .map(mapper::mapEntityToDto);
    }

    @Override
    public void changeRole(UserDto userDto, UserRole newRole) {
        TransactionManager.startTransaction();

        long userId = userDto.getId();
        UserDao userDao = MysqlDaoFactory.getInstance().getUserDao();
        userDao.changeRole(userId, newRole);

        TransactionManager.commit();
    }

    @Override
    public void changePassword(UserDto userDto, String newPassword) {
        TransactionManager.startTransaction();

        long userId = userDto.getId();
        UserDao userDao = MysqlDaoFactory.getInstance().getUserDao();
        userDao.changePassword(userId, newPassword);

        TransactionManager.commit();
    }

    @Override
    public List<UserDto> findAllClients() {
        DtoMapper<User, UserDto> userDtoMapper = DtoMapperFactory.getInstance().getUserDtoMapper();
        return MysqlDaoFactory.getInstance()
                .getUserDao()
                .getAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.CLIENT)
                .map(userDtoMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
