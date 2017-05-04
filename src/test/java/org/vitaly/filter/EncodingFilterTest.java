package org.vitaly.filter;

import org.junit.Test;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-05-04.
 */
public class EncodingFilterTest {

    @Test
    public void doFilterSetsCharacterEncodingToSuppliedEncodingInitParameter() throws Exception {
        FilterConfig filterConfig = mock(FilterConfig.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        Filter encodingFilter = new EncodingFilter();

        String initEncodingParam = "Big5";
        when(filterConfig.getInitParameter(EncodingFilter.ENCODING_INIT_PARAM)).thenReturn(initEncodingParam);
        when(request.getCharacterEncoding()).thenReturn("random encoding");
        encodingFilter.init(filterConfig);
        encodingFilter.doFilter(request, response, filterChain);

        verify(request).setCharacterEncoding(initEncodingParam);
        verify(response).setCharacterEncoding(initEncodingParam);
    }

    @Test
    public void doFilterSetsCharacterEncodingToDefaultIfNoEncodingInitParameterSupplied() throws Exception {
        FilterConfig filterConfig = mock(FilterConfig.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        Filter encodingFilter = new EncodingFilter();

        when(filterConfig.getInitParameter(EncodingFilter.ENCODING_INIT_PARAM)).thenReturn(null);
        when(request.getCharacterEncoding()).thenReturn("random encoding");
        encodingFilter.init(filterConfig);
        encodingFilter.doFilter(request, response, filterChain);

        verify(request).setCharacterEncoding(EncodingFilter.DEFAULT_ENCODING);
        verify(response).setCharacterEncoding(EncodingFilter.DEFAULT_ENCODING);
    }
}