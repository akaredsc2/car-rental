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

    private Map<UrlHttpMethodPair, Set<UserRole>> permissionMap;

    private SecurityManager() {
        Set<UserRole> guest = Collections.singleton(UserRole.GUEST);
        Set<UserRole> admin = Collections.singleton(UserRole.ADMIN);

        Set<UserRole> clientAndAdmin = Collections.unmodifiableSet(
                Stream.of(UserRole.CLIENT, UserRole.ADMIN)
                        .collect(Collectors.toSet()));

        Set<UserRole> all = Collections.unmodifiableSet(
                Stream.of(UserRole.values())
                        .collect(Collectors.toSet()));

        HashMap<UrlHttpMethodPair, Set<UserRole>> permissionMap = new HashMap<>();

        permissionMap.put(UrlHttpMethodPair.ENTRY_POINT, all);
        permissionMap.put(UrlHttpMethodPair.PAGE_INDEX, all);
        permissionMap.put(UrlHttpMethodPair.PAGE_HOME, clientAndAdmin);

        permissionMap.put(UrlHttpMethodPair.ERROR_PAGE_404, all);
        permissionMap.put(UrlHttpMethodPair.ERROR_PAGE_403, all);
        permissionMap.put(UrlHttpMethodPair.ERROR_PAGE, all);

        permissionMap.put(UrlHttpMethodPair.PAGE_REGISTRATION, guest);
        permissionMap.put(UrlHttpMethodPair.PAGE_ADD_MODEL, admin);
        permissionMap.put(UrlHttpMethodPair.PAGE_ADD_LOCATION, admin);

        permissionMap.put(UrlHttpMethodPair.SIGN_IN, guest);
        permissionMap.put(UrlHttpMethodPair.SIGN_OUT, clientAndAdmin);
        permissionMap.put(UrlHttpMethodPair.REGISTRATION, guest);

        permissionMap.put(UrlHttpMethodPair.CHANGE_LOCALE, all);

        permissionMap.put(UrlHttpMethodPair.ADD_MODEL_POST, admin);
        permissionMap.put(UrlHttpMethodPair.ADD_CAR_GET, admin);
        permissionMap.put(UrlHttpMethodPair.ADD_CAR_POST, admin);
        permissionMap.put(UrlHttpMethodPair.ADD_LOCATION_POST, admin);
        permissionMap.put(UrlHttpMethodPair.MOVE_CAR_GET, admin);
        permissionMap.put(UrlHttpMethodPair.MOVE_CAR_POST, admin);

        permissionMap.put(UrlHttpMethodPair.LOCATIONS_GET, clientAndAdmin);
        permissionMap.put(UrlHttpMethodPair.MODELS_GET, clientAndAdmin);
        permissionMap.put(UrlHttpMethodPair.CARS_GET, clientAndAdmin);

        this.permissionMap = Collections.unmodifiableMap(permissionMap);
    }

    public static SecurityManager getInstance() {
        return instance;
    }

    public boolean isValidRequest(HttpServletRequest request) {
        UrlHttpMethodPair urlHttpMethodPair = UrlHttpMethodPair.fromRequest(request);

        // TODO: 30.04.17 remove. figure out why logger is not working
        System.out.println(urlHttpMethodPair);

        return permissionMap
                .keySet()
                .contains(urlHttpMethodPair);
    }

    public boolean isRequestAllowed(HttpServletRequest request) {
        UrlHttpMethodPair urlHttpMethodPair = UrlHttpMethodPair.fromRequest(request);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SESSION_USER) == null) {
            return permissionMap
                    .get(urlHttpMethodPair)
                    .contains(UserRole.GUEST);
        } else {
            UserDto userDto = (UserDto) session.getAttribute(SESSION_USER);
            UserRole role = userDto.getRole();

            return permissionMap
                    .getOrDefault(urlHttpMethodPair, Collections.emptySet())
                    .contains(role);
        }
    }
}
