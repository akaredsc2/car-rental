package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 2017-04-27.
 */
public class RegistrationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto userDto = RequestMapperFactory.getInstance()
                .getUserRequestMapper()
                .map(request);

        // TODO: 2017-04-27 validation

        boolean isRegistered = ServiceFactory.getInstance()
                .getUserService()
                .registerNewUser(userDto);

        if (isRegistered) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            request.setAttribute(ATTR_ERROR, "registration failed");
            request.getServletContext().getRequestDispatcher("/pages/error/error.jsp").forward(request, response);
        }
    }
}
