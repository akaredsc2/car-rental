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

import static org.vitaly.controller.abstraction.validation.Validator.ERR_CANCEL_RES;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 06.05.17.
 */
public class CancelReservationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationDto reservationDto = RequestMapperFactory.getInstance()
                .getReservationRequestMapper()
                .map(request);

        UserDto clientDto = (UserDto) request.getSession().getAttribute(SESSION_USER);
        reservationDto.setClient(clientDto);

        boolean isStateChanged = ServiceFactory.getInstance()
                .getReservationService()
                .cancelReservation(reservationDto);

        if (isStateChanged) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_CANCEL_RES);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
