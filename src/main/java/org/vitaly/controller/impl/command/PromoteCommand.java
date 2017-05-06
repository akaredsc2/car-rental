package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.constants.Pages.HOME_JSP;

/**
 * Created by vitaly on 02.05.17.
 */
public class PromoteCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto userDto = RequestMapperFactory.getInstance()
                .getUserRequestMapper()
                .map(request);

        // TODO: 02.05.17 validate client has no reservations etc
        ServiceFactory.getInstance()
                .getUserService()
                .changeRole(userDto, UserRole.ADMIN);

        response.sendRedirect(request.getContextPath() + HOME_JSP);
    }
}