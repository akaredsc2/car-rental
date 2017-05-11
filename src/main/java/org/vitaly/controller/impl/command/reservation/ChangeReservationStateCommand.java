package org.vitaly.controller.impl.command.reservation;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_CHANGE_RES;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 2017-05-05.
 */
public class ChangeReservationStateCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationDto reservationDto = RequestMapperFactory.getInstance()
                .getReservationRequestMapper()
                .map(request);

        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getChangeReservationStateValidator()
                .validate(reservationDto);

        if (validationResult.isValid()) {
            doChangeReservationState(request, response, reservationDto);
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }

    private void doChangeReservationState(HttpServletRequest request,
                                          HttpServletResponse response, ReservationDto reservationDto)
            throws IOException, ServletException {
        UserDto adminDto = (UserDto) request.getSession().getAttribute(SESSION_USER);
        reservationDto.setAdmin(adminDto);


        ReservationState newState = reservationDto.getState();
        boolean isStateChanged = ServiceFactory.getInstance()
                .getReservationService()
                .changeReservationState(reservationDto, newState.toString());

        if (isStateChanged) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_CHANGE_RES);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
