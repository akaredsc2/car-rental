package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.INDEX_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_PASS_REPEAT;

/**
 * Created by vitaly on 2017-04-27.
 */
public class RegistrationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ValidationResult validationResult = ValidatorFactory.getInstance().getRegistrationValidator().validate(request);

        if (validationResult.isValid()) {
            UserDto userDto = RequestMapperFactory.getInstance()
                    .getUserRequestMapper()
                    .map(request);

            boolean isRegistered = ServiceFactory.getInstance()
                    .getUserService()
                    .registerNewUser(userDto);

            if (isRegistered) {
                response.sendRedirect(request.getContextPath() + INDEX_JSP);
            } else {
                request.setAttribute(ATTR_ERROR, "registration failed");
                request.getServletContext()
                        .getRequestDispatcher(ERROR_JSP)
                        .forward(request, response);
            }
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
