package org.vitaly.service.implementation;

import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.UserService;

import java.util.List;
import java.util.Optional;

//import org.vitaly.template.abstraction.DaoFactory;

/**
 * Created by vitaly on 2017-04-10.
 */
public class UserServiceImpl implements UserService {
    private UserDao dao;

    public UserServiceImpl(PooledConnection connection) {
//        this.template = DaoFactory.getMysqlDaoFactory().createUserDao(connection);
    }

    @Override
    public Optional<User> findById(long id) {
        return dao.findById(id);
    }

    @Override
    public Optional<Long> findIdOfEntity(User user) {
        return dao.findIdOfEntity(user);
    }

    @Override
    public List<User> getAll() {
        return dao.getAll();
    }

    @Override
    public Optional<Long> create(User user) {
        return dao.create(user);
    }

    @Override
    public Optional<User> authenticate(String login, String password) {
        return dao.authenticate(login, password);
    }

    @Override
    public void changeRole(long userId, UserRole role) {
        dao.changeRole(userId, role);
    }

    @Override
    public void changePassword(long userId, String newPassword) {
        dao.changePassword(userId, newPassword);
    }
}
