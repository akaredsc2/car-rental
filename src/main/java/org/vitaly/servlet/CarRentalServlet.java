package org.vitaly.servlet;

import org.vitaly.controller.impl.factory.CommandFactory;
import org.vitaly.security.CommandHttpMethodPair;
import org.vitaly.security.SecurityManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.constants.Pages.ERROR_403_JSP;

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
        CommandHttpMethodPair commandHttpMethodPair = CommandHttpMethodPair.fromRequest(req);

        boolean isPermissionGranted = SecurityManager.getInstance()
                .checkPermission(req, commandHttpMethodPair);

        if (isPermissionGranted) {
            CommandFactory.getInstance()
                    .getCommand(commandHttpMethodPair)
                    .execute(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + ERROR_403_JSP);
        }
    }
}
