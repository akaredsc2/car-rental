package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.vitaly.model.reservation.ReservationStateEnum.*;

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

        boolean isUserCreated = MysqlDaoFactory.getInstance()
                .getUserDao()
                .create(user)
                .isPresent();

        TransactionManager.commit();

        return isUserCreated;
    }

    @Override
    public Optional<UserDto> authenticate(String login, String password) {
        DtoMapper<User, UserDto> mapper = DtoMapperFactory.getInstance().getUserDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getUserDao()
                .authenticate(login, password)
                .map(mapper::mapEntityToDto);
    }

    @Override
    public boolean changeRole(UserDto userDto, UserRole newRole) {
        TransactionManager.startTransaction();

        long userId = userDto.getId();
        MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

        boolean userHasActiveReservations = daoFactory
                .getReservationDao()
                .findReservationsByClientId(userId)
                .stream()
                .map(Reservation::getState)
                .anyMatch(this::isNotTerminated);

        if (userHasActiveReservations) {
            TransactionManager.rollback();
            return false;
        } else {
            daoFactory.getUserDao()
                    .changeRole(userId, newRole);

            TransactionManager.commit();
            return true;
        }
    }

    private boolean isNotTerminated(ReservationState state) {
        return state == NEW.getState() || state == APPROVED.getState() || state == ACTIVE.getState();
    }

    @Override
    public boolean changePassword(UserDto userDto, String newPassword) {
        TransactionManager.startTransaction();

        UserDao userDao = MysqlDaoFactory.getInstance().getUserDao();

        boolean isOldPasswordCorrect = userDao.authenticate(userDto.getLogin(), userDto.getPassword())
                .isPresent();

        if (isOldPasswordCorrect) {
            long userId = userDto.getId();
            userDao.changePassword(userId, newPassword);

            TransactionManager.commit();
            return true;
        } else {
            TransactionManager.rollback();
            return false;
        }
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
