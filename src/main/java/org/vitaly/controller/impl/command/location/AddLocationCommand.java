package org.vitaly.controller.impl.command.location;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_ADD_LOC;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 28.04.17.
 */
public class AddLocationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocationDto locationDto = RequestMapperFactory.getInstance()
                .getLocationRequestMapper()
                .map(request);

        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getAddLocationValidator()
                .validate(locationDto);

        if (validationResult.isValid()) {
            doAddLocation(request, response, locationDto);
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }

    private void doAddLocation(HttpServletRequest request, HttpServletResponse response, LocationDto locationDto) throws IOException, ServletException {
        boolean isAdded = ServiceFactory.getInstance()
                .getLocationService()
                .addNewLocation(locationDto);

        if (isAdded) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_ADD_LOC);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
