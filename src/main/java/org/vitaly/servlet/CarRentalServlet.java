package org.vitaly.servlet;

import org.vitaly.controller.impl.factory.CommandFactory;
import org.vitaly.security.UrlHttpMethodPair;
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
        // TODO: 30.04.17 ask. same thing created 2 times in filters and 1 time here. is that ok?
        UrlHttpMethodPair urlHttpMethodPair = UrlHttpMethodPair.fromRequest(req);

        CommandFactory.getInstance()
                .getCommand(urlHttpMethodPair)
                .execute(req, resp);
    }
}
