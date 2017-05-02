package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;
import org.vitaly.util.constants.RequestParameters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;

/**
 * Created by vitaly on 02.05.17.
 */
public class ChangeCarStateCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        String newCarStateParam = properties.getProperty(RequestParameters.PARAM_CAR_STATE_NEW);
        String newCarStateString = request.getParameter(newCarStateParam);

        CarDto carDto = RequestMapperFactory.getInstance()
                .getCarRequestMapper()
                .map(request);

        boolean isStateChanged = ServiceFactory.getInstance()
                .getCarService()
                .changeCarState(carDto, newCarStateString);

        if (isStateChanged) {
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            request.setAttribute(ATTR_ERROR, "Failed to change car state");
            request.getServletContext()
                    .getRequestDispatcher("/pages/error/error.jsp")
                    .forward(request, response);
        }
    }
}
