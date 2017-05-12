package org.vitaly.security;

import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 30.04.17.
 */
public class SecurityManager {
    private static SecurityManager instance = new SecurityManager();

    private Map<CommandHttpMethodPair, Set<UserRole>> permissionMap;

    private SecurityManager() {
        Set<UserRole> guest = Collections.singleton(UserRole.GUEST);
        Set<UserRole> client = Collections.singleton(UserRole.CLIENT);
        Set<UserRole> admin = Collections.singleton(UserRole.ADMIN);

        Set<UserRole> clientAndAdmin = Collections.unmodifiableSet(
                Stream.of(UserRole.CLIENT, UserRole.ADMIN)
                        .collect(Collectors.toSet()));

        Set<UserRole> all = Collections.unmodifiableSet(
                Stream.of(UserRole.values())
                        .collect(Collectors.toSet()));

        HashMap<CommandHttpMethodPair, Set<UserRole>> permissionMap = new HashMap<>();

        permissionMap.put(CommandHttpMethodPair.SIGN_IN_POST, guest);
        permissionMap.put(CommandHttpMethodPair.SIGN_OUT_POST, clientAndAdmin);
        permissionMap.put(CommandHttpMethodPair.REGISTRATION_POST, guest);
        permissionMap.put(CommandHttpMethodPair.CHANGE_PASSWORD_POST, clientAndAdmin);

        permissionMap.put(CommandHttpMethodPair.CHANGE_LOCALE, all);

        permissionMap.put(CommandHttpMethodPair.ADD_MODEL_POST, admin);
        permissionMap.put(CommandHttpMethodPair.ADD_CAR_GET, admin);
        permissionMap.put(CommandHttpMethodPair.ADD_CAR_POST, admin);
        permissionMap.put(CommandHttpMethodPair.ADD_LOCATION_POST, admin);
        permissionMap.put(CommandHttpMethodPair.MOVE_CAR_GET, admin);
        permissionMap.put(CommandHttpMethodPair.MOVE_CAR_POST, admin);
        permissionMap.put(CommandHttpMethodPair.CHANGE_CAR_STATE_POST, admin);
        permissionMap.put(CommandHttpMethodPair.UPDATE_CAR_POST, admin);
        permissionMap.put(CommandHttpMethodPair.PROMOTE_GET, admin);
        permissionMap.put(CommandHttpMethodPair.PROMOTE_POST, admin);
        permissionMap.put(CommandHttpMethodPair.CREATE_RESERVATION_POST, client);
        permissionMap.put(CommandHttpMethodPair.ASSIGN_POST, admin);
        permissionMap.put(CommandHttpMethodPair.CHANGE_RESERVATION_STATE_POST, admin);
        permissionMap.put(CommandHttpMethodPair.PAY_POST, client);
        permissionMap.put(CommandHttpMethodPair.CONFIRM_POST, admin);
        permissionMap.put(CommandHttpMethodPair.CANCEL_RESERVATION_POST, client);
        permissionMap.put(CommandHttpMethodPair.ADD_DAMAGE_BILL_POST, admin);
        permissionMap.put(CommandHttpMethodPair.UPDATE_LOCATION_POST, admin);
        permissionMap.put(CommandHttpMethodPair.UPDATE_MODEL_POST, admin);

        permissionMap.put(CommandHttpMethodPair.LOCATIONS_GET, clientAndAdmin);
        permissionMap.put(CommandHttpMethodPair.MODELS_GET, clientAndAdmin);
        permissionMap.put(CommandHttpMethodPair.CARS_GET, clientAndAdmin);
        permissionMap.put(CommandHttpMethodPair.RESERVATIONS_GET, clientAndAdmin);
        permissionMap.put(CommandHttpMethodPair.BILLS_GET, clientAndAdmin);

        this.permissionMap = Collections.unmodifiableMap(permissionMap);
    }

    public static SecurityManager getInstance() {
        return instance;
    }

    public boolean isValidRequest(HttpServletRequest request) {
        CommandHttpMethodPair commandHttpMethodPair = CommandHttpMethodPair.fromRequest(request);

        return permissionMap
                .keySet()
                .contains(commandHttpMethodPair);
    }

    public boolean checkPermission(HttpServletRequest request, CommandHttpMethodPair commandHttpMethodPair) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SESSION_USER) == null) {
            return permissionMap
                    .get(commandHttpMethodPair)
                    .contains(UserRole.GUEST);
        } else {
            UserDto userDto = (UserDto) session.getAttribute(SESSION_USER);
            UserRole role = userDto.getRole();

            return permissionMap
                    .getOrDefault(commandHttpMethodPair, Collections.emptySet())
                    .contains(role);
        }
    }
}
