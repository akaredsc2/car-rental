package org.vitaly.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter that changes character encoding
 */
public class EncodingFilter implements Filter {
    static final String DEFAULT_ENCODING = "UTF-8";
    static final String ENCODING_INIT_PARAM = "encoding";

    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter(ENCODING_INIT_PARAM);

        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
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
