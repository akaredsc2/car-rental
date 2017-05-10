package org.vitaly.controller.impl.command.reservation;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_CREATE_RES;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 2017-05-05.
 */
public class CreateReservationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getReservationValidator()
                .validate(request);

        if (validationResult.isValid()) {
            doCreateReservation(request, response);
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }

    private void doCreateReservation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ReservationDto reservationDto = RequestMapperFactory.getInstance()
                .getReservationRequestMapper()
                .map(request);

        UserDto userDto = (UserDto) request.getSession().getAttribute(SESSION_USER);
        reservationDto.setClient(userDto);

        boolean isReservationCreated = ServiceFactory.getInstance()
                .getReservationService()
                .createNewReservation(reservationDto);

        if (isReservationCreated) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_CREATE_RES);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
