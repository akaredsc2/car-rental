package org.vitaly.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by vitaly on 2017-04-27.
 */
public class EncodingFilter implements Filter {
    private static final String DEFAULT_ENCODING = "UTF-8";

    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");

        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestEncoding = httpRequest.getCharacterEncoding();

        if (!encoding.equalsIgnoreCase(requestEncoding)) {
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        encoding = null;
    }
}
