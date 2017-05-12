package org.vitaly.controller.impl.command.user;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.abstraction.validation.ValidationResult;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_CHANGE_PASSWORD;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_PASS_NEW;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 02.05.17.
 */
public class ChangePasswordCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ValidationResult validationResult = ValidatorFactory.getInstance()
                .getChangePasswordValidator()
                .validate(request);

        if (validationResult.isValid()) {
            doChangePassword(request, response);
        } else {
            request.setAttribute(ATTR_ERROR, validationResult.getErrorMessages());
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }

    private void doChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        String newPassword = request.getParameter(properties.getProperty(PARAM_PASS_NEW));

        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute(SESSION_USER);

        boolean isPasswordChanged = ServiceFactory.getInstance()
                .getUserService()
                .changePassword(userDto, newPassword);

        if (isPasswordChanged) {
            userDto.setPassword(newPassword);
            session.setAttribute(SESSION_USER, userDto);

            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_CHANGE_PASSWORD);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
