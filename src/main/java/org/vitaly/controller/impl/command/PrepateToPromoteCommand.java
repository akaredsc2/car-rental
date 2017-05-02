package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.vitaly.util.constants.RequestAttributes.ATTR_USER_LIST;

/**
 * Created by vitaly on 02.05.17.
 */
public class PrepateToPromoteCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserDto> userDtoList = ServiceFactory.getInstance()
                .getUserService()
                .findAllClients();

        request.setAttribute(ATTR_USER_LIST, userDtoList);

        request.getServletContext()
                .getRequestDispatcher("/pages/admin/promote.jsp")
                .forward(request, response);
    }
}
