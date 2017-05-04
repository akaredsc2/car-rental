package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.RequestParameters.*;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 02.05.17.
 */
public class ChangePasswordCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        String oldPassParam = properties.getProperty(PARAM_PASS_OLD);
        String newPassParam = properties.getProperty(PARAM_PASS_NEW);
        String repeatPassParam = properties.getProperty(PARAM_PASS_REPEAT);

        // TODO: 02.05.17 validate
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute(SESSION_USER);

        String newPassword = request.getParameter(newPassParam);

        boolean isPasswordChanged = ServiceFactory.getInstance()
                .getUserService()
                .changePassword(userDto, newPassword);

        if (isPasswordChanged) {
            userDto.setPassword(newPassword);
            session.setAttribute(SESSION_USER, userDto);

            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, "Failed to change password!");
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
