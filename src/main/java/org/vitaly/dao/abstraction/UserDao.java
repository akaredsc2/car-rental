package org.vitaly.dao.abstraction;

import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.util.Optional;

/**
 * Created by vitaly on 2017-03-28.
 */
public interface UserDao extends AbstractDao<User> {
    Optional<User> authenticate(String login, String password);

    void changeRole(User user, UserRole role);

    void changePassword(User user, String newPassword);
}