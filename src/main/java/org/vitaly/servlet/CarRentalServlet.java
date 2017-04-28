package org.vitaly.servlet;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.CommandFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.util.PropertyNames.PARAMETERS;
import static org.vitaly.util.PropertyNames.PARAM_COMMAND;

/**
 * Created by vitaly on 2017-04-11.
 */
public class CarRentalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        String commandParameter = req.getParameter(properties.getProperty(PARAM_COMMAND));

        Command command = CommandFactory.getInstance().getCommand(commandParameter);
        command.execute(req, resp);
    }
}
