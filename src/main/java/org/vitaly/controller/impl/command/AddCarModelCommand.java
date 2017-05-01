package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 28.04.17.
 */
public class AddCarModelCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CarModelDto carModelDto = RequestMapperFactory.getInstance()
                .getCarModelRequestMapper()
                .map(request);

        // TODO: 28.04.17 validation

        boolean isAdded = ServiceFactory.getInstance()
                .getCarModelService()
                .addCarModel(carModelDto);

        if (isAdded) {
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            request.setAttribute(ATTR_ERROR, "failed to add car model");
            request.getServletContext().getRequestDispatcher("/pages/error/error.jsp").forward(request, response);
        }
    }
}
