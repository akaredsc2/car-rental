package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.PropertyNames.ATTR_ERROR;

/**
 * Created by vitaly on 29.04.17.
 */
public class AddCarCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CarDto carDto = RequestMapperFactory.getInstance()
                .getCarRequestMapper()
                .map(request);

        // TODO: 29.04.17 validation

        boolean isAdded = ServiceFactory.getInstance()
                .getCarService()
                .addNewCar(carDto);

        if (isAdded) {
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            request.setAttribute(ATTR_ERROR, "failed to add car");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}