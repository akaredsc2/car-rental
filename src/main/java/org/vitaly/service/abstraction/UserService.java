package org.vitaly.service.abstraction;

import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;

import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface UserService {
    Optional<Long> registerNewUser(UserDto userDto);

    Optional<User> authenticate(String login, String password);

    void changeRole(UserDto userDto, UserRole role);

    void changePassword(UserDto userDto, String newPassword);
}
