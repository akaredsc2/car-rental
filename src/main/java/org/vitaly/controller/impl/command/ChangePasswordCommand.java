package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;

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
        UserDto userDto = RequestMapperFactory.getInstance()
                .getUserRequestMapper()
                .map(request);

        String newPassword = request.getParameter(newPassParam);

        ServiceFactory.getInstance()
                .getUserService()
                .changePassword(userDto, newPassword);

        response.sendRedirect(request.getContextPath() + "/home.jsp");
    }
}
