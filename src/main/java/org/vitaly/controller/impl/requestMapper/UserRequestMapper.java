package org.vitaly.controller.impl.requestMapper;

import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by vitaly on 2017-04-27.
 */
public class UserRequestMapper implements RequestMapper<UserDto> {

    @Override
    public UserDto map(HttpServletRequest request) {
        String idString = request.getParameter("param_user_id");
        long id = (idString == null) ? 0 : Long.valueOf(idString);

        String login = request.getParameter("param_user_login");
        String password = request.getParameter("param_user_password");
        String fullName = request.getParameter("param_user_full_name");

        String birthDateString = request.getParameter("param_user_birth_date");
        LocalDate birthDate =
                (birthDateString == null) ?
                        null :
                        LocalDateTime.parse(birthDateString, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();

        String passportNumber = request.getParameter("param_user_passport_number");
        String licenceNumber = request.getParameter("param_user_driver_licence_number");
        String roleString = request.getParameter("param_user_role");

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
        userDto.setBirthDate(birthDate);
        userDto.setPassportNumber(passportNumber);
        userDto.setDriverLicenceNumber(licenceNumber);
        userDto.setRole(userRole);
        userDto.setReservationDtoList(Collections.emptyList());
        userDto.setNotificationDtoList(Collections.emptyList());

        return userDto;
    }
}
