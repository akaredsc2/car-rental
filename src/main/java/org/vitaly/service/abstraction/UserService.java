package org.vitaly.service.abstraction;

import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface UserService {
    Optional<User> findById(long id);

    Optional<Long> findIdOfEntity(User entity);

    List<User> getAll();

    Optional<Long> create(User entity);

    Optional<User> authenticate(String login, String password);

    void changeRole(long userId, UserRole role);

    void changePassword(long userId, String newPassword);
}
