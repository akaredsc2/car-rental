package org.vitaly.controller.impl.command.car;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_ADD_CAR;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 29.04.17.
 */
public class AddCarCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CarDto carDto = RequestMapperFactory.getInstance()
                .getCarRequestMapper()
                .map(request);

        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getAddCarValidator()
                .validate(carDto);

        if (validationResult.isValid()) {
            doAddCar(request, response, carDto);
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }

    private void doAddCar(HttpServletRequest request, HttpServletResponse response, CarDto carDto)
            throws IOException, ServletException {
        boolean isAdded = ServiceFactory.getInstance()
                .getCarService()
                .addNewCar(carDto);

        if (isAdded) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_ADD_CAR);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
