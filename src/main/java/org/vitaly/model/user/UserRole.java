package org.vitaly.model.user;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by vitaly on 2017-03-28.
 */
public enum UserRole {
    GUEST,
    CLIENT,
    ADMIN;

    public static Optional<UserRole> of(String roleName) {
        return Arrays.stream(UserRole.values())
                .map(UserRole::toString)
                .filter(v -> v.equalsIgnoreCase(roleName))
                .map(UserRole::valueOf)
                .findFirst();
    }
}
