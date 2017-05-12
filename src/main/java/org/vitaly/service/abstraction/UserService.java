package org.vitaly.service.abstraction;

import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public interface UserService {
    boolean registerNewUser(UserDto userDto);

    Optional<UserDto> authenticate(String login, String password);

    boolean changeRole(UserDto userDto, UserRole role);

    boolean changePassword(UserDto userDto, String newPassword);

    List<UserDto> findAllClients();
}
