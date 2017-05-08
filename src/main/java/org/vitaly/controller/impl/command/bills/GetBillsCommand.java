package org.vitaly.controller.impl.command.bills;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.BillDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.vitaly.util.constants.Pages.BILLS_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_BILL_LIST;

/**
 * Created by vitaly on 08.05.17.
 */
public class GetBillsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationDto reservationDto = RequestMapperFactory.getInstance()
                .getReservationRequestMapper()
                .map(request);

        List<BillDto> billDtoList = ServiceFactory.getInstance()
                .getBillService()
                .findBillsForReservation(reservationDto);

        request.setAttribute(ATTR_BILL_LIST, billDtoList);
        request.getServletContext()
                .getRequestDispatcher(BILLS_JSP)
                .forward(request, response);
    }
}
