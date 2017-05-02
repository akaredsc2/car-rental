package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 02.05.17.
 */
public class UpdateCarCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CarDto carDto = RequestMapperFactory.getInstance()
                .getCarRequestMapper()
                .map(request);

        boolean isUpdated = ServiceFactory.getInstance()
                .getCarService()
                .updateCar(carDto);

        if (isUpdated) {
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            request.setAttribute(ATTR_ERROR, "Failed to update car!");
            request.getServletContext()
                    .getRequestDispatcher("/pages/error/error.jsp")
                    .forward(request, response);
        }
    }
}
