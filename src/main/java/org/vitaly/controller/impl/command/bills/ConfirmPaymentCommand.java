package org.vitaly.controller.impl.command.bills;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_CONFIRM_BILL;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 08.05.17.
 */
public class ConfirmPaymentCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BillDto billDto = RequestMapperFactory.getInstance()
                .getBillRequestMapper()
                .map(request);

        boolean isConfirmed = ServiceFactory.getInstance()
                .getBillService()
                .markConfirmed(billDto);

        if (isConfirmed) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_CONFIRM_BILL);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
