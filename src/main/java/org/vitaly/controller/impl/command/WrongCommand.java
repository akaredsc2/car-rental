package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.controller.abstraction.validation.Validator.ERR_WRONG_COMMAND;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 2017-04-27.
 */
public class WrongCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ATTR_ERROR, ERR_WRONG_COMMAND);
        request.getServletContext()
                .getRequestDispatcher(ERROR_JSP)
                .forward(request, response);
    }
}
