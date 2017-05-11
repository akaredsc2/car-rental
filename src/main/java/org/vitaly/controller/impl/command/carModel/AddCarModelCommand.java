package org.vitaly.controller.impl.command.carModel;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_ADD_CAR_MODEL;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
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

        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getAddModelValidator()
                .validate(carModelDto);

        if (validationResult.isValid()) {
            doAddModel(request, response, carModelDto);
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }

    private void doAddModel(HttpServletRequest request, HttpServletResponse response, CarModelDto carModelDto)
            throws IOException, ServletException {
        boolean isAdded = ServiceFactory.getInstance()
                .getCarModelService()
                .addCarModel(carModelDto);

        if (isAdded) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_ADD_CAR_MODEL);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
