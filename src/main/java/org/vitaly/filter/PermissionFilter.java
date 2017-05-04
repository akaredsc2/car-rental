package org.vitaly.filter;

import org.vitaly.security.SecurityManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.vitaly.util.constants.Pages.ERROR_403_JSP;

/**
 * Created by vitaly on 30.04.17.
 */
public class PermissionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        boolean isRequestAllow = SecurityManager.getInstance()
                .isRequestAllowed(httpServletRequest);

        if (isRequestAllow) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response)
                    .sendRedirect(httpServletRequest.getContextPath() + ERROR_403_JSP);
        }
    }

    @Override
    public void destroy() {
    }
}
