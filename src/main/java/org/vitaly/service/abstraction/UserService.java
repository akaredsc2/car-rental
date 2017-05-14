package org.vitaly.service.abstraction;

import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * User service
 */
public interface UserService {
    /**
     * Register new user
     *
     * @param userDto user dto
     * @return true if registered
     */
    boolean registerNewUser(UserDto userDto);

    /**
     * Authenticate user with login and password
     *
     * @param login    login
     * @param password password
     * @return user if authenticated, empty optional otherwise
     */
    Optional<UserDto> authenticate(String login, String password);

    /**
     * Change user role
     *
     * @param userDto user
     * @param role    role
     * @return true if changed, false otherwise
     */
    boolean changeRole(UserDto userDto, UserRole role);

    /**
     * Change password
     *
     * @param userDto     user
     * @param newPassword new password
     * @return true if changed, false otherwise
     */
    boolean changePassword(UserDto userDto, String newPassword);

    /**
     * Find all clients
     *
     * @return list of all clients
     */
    List<UserDto> findAllClients();
}
