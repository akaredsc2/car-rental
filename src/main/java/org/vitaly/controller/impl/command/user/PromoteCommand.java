package org.vitaly.controller.impl.command.user;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_CHANGE_ROLE;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 02.05.17.
 */
public class PromoteCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto userDto = RequestMapperFactory.getInstance()
                .getUserRequestMapper()
                .map(request);

        boolean isRoleChanged = ServiceFactory.getInstance()
                .getUserService()
                .changeRole(userDto, UserRole.ADMIN);

        if (isRoleChanged) {
            response.sendRedirect(request.getContextPath() + HOME_JSP);
        } else {
            request.setAttribute(ATTR_ERROR, ERR_CHANGE_ROLE);
            request.getServletContext()
                    .getRequestDispatcher(ERROR_JSP)
                    .forward(request, response);
        }
    }
}
