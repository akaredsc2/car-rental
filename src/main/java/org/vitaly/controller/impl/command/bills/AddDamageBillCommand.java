package org.vitaly.controller.impl.command.bills;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_ADD_BILL;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 08.05.17.
 */
public class AddDamageBillCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

        BillDto billDto = requestMapperFactory
                .getBillRequestMapper()
                .map(request);

        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getAddDamageBillValidator()
                .validate(billDto);

        if (validationResult.isValid()) {
            doAddDamageBill(request, response, requestMapperFactory, billDto);
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }

    private void doAddDamageBill(HttpServletRequest request, HttpServletResponse response,
                                 RequestMapperFactory requestMapperFactory, BillDto billDto)
            throws IOException, ServletException {
        ReservationDto reservationDto = requestMapperFactory
                .getReservationRequestMapper()
                .map(request);

        boolean isBillAdded = ServiceFactory.getInstance()
                .getBillService()
                .addDamageBillToReservation(billDto, reservationDto);

        if (isBillAdded) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_ADD_BILL);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
