package org.vitaly.filter;

import org.vitaly.security.SecurityManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vitaly on 30.04.17.
 */
public class UrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        boolean isValidRequest = SecurityManager.getInstance()
                .isValidRequest(httpServletRequest);

        if (isValidRequest) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response)
                    .sendRedirect(httpServletRequest.getContextPath() + "/404.jsp");
        }
    }

    @Override
    public void destroy() {
    }
}
