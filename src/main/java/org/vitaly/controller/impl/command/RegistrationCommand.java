package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.controller.impl.requestMapper.UserRequestMapper;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vitaly on 2017-04-27.
 */
public class RegistrationCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestMapper<UserDto> userMapper = new UserRequestMapper();

        UserDto userDto = userMapper.map(request);

        // TODO: 2017-04-27 validation

        boolean isRegistered = ServiceFactory.getInstance()
                .getUserService()
                .registerNewUser(userDto);

        if (isRegistered) {
            request.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "registration failed");
            request.getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
