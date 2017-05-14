package org.vitaly.model.user;

import java.util.Arrays;
import java.util.Optional;

/**
 * Elements of this enum represents roles of user in system.
 *
 * @author vitaly
 */
public enum UserRole {

    /**
     * Guest is an unauthorized user. He can only sing in or sign up.
     */
    GUEST,

    /**
     * Client is an authorized user. He can create reserve cars.
     */
    CLIENT,

    /**
     * Admin is an authorized user. He can fill system with data and manage it.
     */
    ADMIN;

    /**
     * Exception-less version of valueOf method with ignore case
     *
     * @param roleName string to parse as role. Case does not matter
     * @return optional with role if such role exists and empty optional if not
     * @see UserRole#valueOf(String)
     */
    public static Optional<UserRole> of(String roleName) {
        return Arrays.stream(UserRole.values())
                .map(UserRole::toString)
                .filter(v -> v.equalsIgnoreCase(roleName))
                .map(UserRole::valueOf)
                .findFirst();
    }
}
