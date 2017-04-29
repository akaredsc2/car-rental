package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.util.PropertyNames.ATTR_ERROR;
import static org.vitaly.util.PropertyNames.PARAMETERS;

/**
 * Created by vitaly on 2017-04-27.
 */
public class WrongCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ATTR_ERROR, "wrong command");
        request.getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
    }
}
