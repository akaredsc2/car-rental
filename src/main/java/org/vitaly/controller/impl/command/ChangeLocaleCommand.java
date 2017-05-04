package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

import static org.vitaly.util.constants.Pages.INDEX_JSP;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_LOCALE;
import static org.vitaly.util.constants.SessionAttributes.SESSION_LOCALE;

/**
 * Created by vitaly on 29.04.17.
 */
public class ChangeLocaleCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties parameters = PropertyUtils.readProperties(PARAMETERS);
        String localeParameter = parameters.getProperty(PARAM_LOCALE);
        String locale = request.getParameter(localeParameter);

        // TODO: 29.04.17 validate

        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_LOCALE, locale);

        // TODO: 29.04.17 consider authenticated user
        // TODO: 29.04.17 consider redirecting to page where from the request came
        response.sendRedirect(request.getContextPath() + INDEX_JSP);
    }
}
