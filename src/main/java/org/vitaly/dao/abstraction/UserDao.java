package org.vitaly.dao.abstraction;

import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.util.Optional;

/**
 * Dao for user
 */
public interface UserDao extends AbstractDao<User> {

    /**
     * Check if user with such password and login exists
     *
     * @param login    login
     * @param password password
     * @return user object if login and password matches to the ones in db or empty optional otherwise
     */
    Optional<User> authenticate(String login, String password);

    /**
     * Change role of user
     *
     * @param userId user id
     * @param role   role
     */
    void changeRole(long userId, UserRole role);

    /**
     * Change password of user
     *
     * @param userId      user id
     * @param newPassword new password
     */
    void changePassword(long userId, String newPassword);
}
