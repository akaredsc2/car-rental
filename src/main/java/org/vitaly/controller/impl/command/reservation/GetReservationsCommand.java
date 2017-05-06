package org.vitaly.controller.impl.command.reservation;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.abstraction.ReservationService;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.vitaly.util.constants.Pages.RESERVATIONS_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_RESERVATION_LIST;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_UNASSIGNED;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 2017-05-05.
 */
public class GetReservationsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto userDto = (UserDto) request.getSession().getAttribute(SESSION_USER);
        ReservationService reservationService = ServiceFactory.getInstance().getReservationService();

        List<ReservationDto> reservationDtoList;
        if (userDto.getRole() == UserRole.CLIENT) {
            reservationDtoList = reservationService.findReservationsOfClient(userDto);
        } else if (userDto.getRole() == UserRole.ADMIN) {
            reservationDtoList = findAdminReservations(request, userDto, reservationService);
        } else {
            reservationDtoList = Collections.emptyList();
        }

        request.setAttribute(ATTR_RESERVATION_LIST, reservationDtoList);

        request.getServletContext()
                .getRequestDispatcher(RESERVATIONS_JSP)
                .forward(request, response);
    }

    private List<ReservationDto> findAdminReservations(HttpServletRequest request, UserDto userDto, ReservationService reservationService) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        String unassignedParam = properties.getProperty(PARAM_UNASSIGNED);

        if (parameterMap.containsKey(unassignedParam)
                && Boolean.parseBoolean(request.getParameter(unassignedParam))) {
            return reservationService.findReservationsWithoutAdmin();
        } else {
            return reservationService.findReservationsAssignedToAdmin(userDto);
        }
    }
}
