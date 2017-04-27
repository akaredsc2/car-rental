package org.vitaly.controller.impl.requestMapper;

import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

import static org.vitaly.util.Properties.*;

/**
 * Created by vitaly on 2017-04-27.
 */
public class UserRequestMapper implements RequestMapper<UserDto> {

    @Override
    public UserDto map(HttpServletRequest request) {
        ResourceBundle parameters = ResourceBundle.getBundle(PARAMETERS);

        String idString = request.getParameter(parameters.getString(PARAM_USER_ID));
        long id = (idString == null) ? 0 : Long.valueOf(idString);

        String login = request.getParameter(parameters.getString(PARAM_USER_LOGIN));
        String password = request.getParameter(parameters.getString(PARAM_USER_PASSWORD));
        String fullName = request.getParameter(parameters.getString(PARAM_USER_NAME));

        String birthDateString = request.getParameter(parameters.getString(PARAM_USER_BIRTHDAY));
        LocalDate birthday =
                (birthDateString == null) ?
                        null :
                        LocalDateTime.parse(birthDateString, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();

        String passportNumber = request.getParameter(parameters.getString(PARAM_USER_PASSPORT));
        String licenceNumber = request.getParameter(parameters.getString(PARAM_USER_DRIVER));
        String roleString = request.getParameter(parameters.getString(PARAM_USER_ROLE));

        UserRole userRole = Arrays.stream(UserRole.values())
                .map(UserRole::toString)
                .filter(v -> v.equalsIgnoreCase(roleString))
                .map(UserRole::valueOf)
                .findFirst()
                .orElse(null);

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setLogin(login);
        userDto.setPassword(password);
        userDto.setFullName(fullName);
        userDto.setBirthDate(birthday);
        userDto.setPassportNumber(passportNumber);
        userDto.setDriverLicenceNumber(licenceNumber);
        userDto.setRole(userRole);
        userDto.setReservationDtoList(Collections.emptyList());
        userDto.setNotificationDtoList(Collections.emptyList());

        return userDto;
    }
}
