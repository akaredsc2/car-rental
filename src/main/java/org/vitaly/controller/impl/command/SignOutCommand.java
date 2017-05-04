package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.vitaly.util.constants.Pages.INDEX_JSP;

/**
 * Created by vitaly on 2017-04-28.
 */
public class SignOutCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + INDEX_JSP);
    }
}
