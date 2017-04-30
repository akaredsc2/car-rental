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

import static org.vitaly.util.PropertyNames.SESSION_USER;

/**
 * Created by vitaly on 2017-04-27.
 */
public class SignInCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestMapper<UserDto> userMapper = new UserRequestMapper();
        UserDto userDto = userMapper.map(request);

        // TODO: 2017-04-28 consider failure
        ServiceFactory.getInstance()
                .getUserService()
                .authenticate(userDto.getLogin(), userDto.getPassword())
                .ifPresent(user -> request.getSession(true).setAttribute(SESSION_USER, user));

        response.sendRedirect(request.getContextPath() + "/home.jsp");
    }
}
