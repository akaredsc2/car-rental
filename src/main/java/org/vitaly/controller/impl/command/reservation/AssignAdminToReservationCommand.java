package org.vitaly.controller.impl.command.reservation;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_ASSIGN_ADMIN;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 2017-05-05.
 */
public class AssignAdminToReservationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto userDto = (UserDto) request.getSession().getAttribute(SESSION_USER);

        ReservationDto reservationDto = RequestMapperFactory.getInstance()
                .getReservationRequestMapper()
                .map(request);

        boolean isAdminAssigned = ServiceFactory.getInstance()
                .getReservationService()
                .assignReservationToAdmin(reservationDto, userDto);

        if (isAdminAssigned) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_ASSIGN_ADMIN);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
