package org.vitaly.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.security.SecurityManager;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-05-04.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityManager.class})
@PowerMockIgnore("javax.management.*")
public class UrlFilterTest {

    @Test
    public void requestValidatedBySecurityManagerGoesToNextFilter() throws Exception {
        SecurityManager securityManager = mock(SecurityManager.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        UrlFilter urlFilter = new UrlFilter();

        PowerMockito.mockStatic(SecurityManager.class);
        PowerMockito.when(SecurityManager.getInstance()).thenReturn(securityManager);
        when(securityManager.isValidRequest(request)).thenReturn(true);
        urlFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void requestInvalidatedBySecurityManagerIsRedirected() throws Exception {
        SecurityManager securityManager = mock(SecurityManager.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        UrlFilter urlFilter = new UrlFilter();

        PowerMockito.mockStatic(SecurityManager.class);
        PowerMockito.when(SecurityManager.getInstance()).thenReturn(securityManager);
        when(securityManager.isValidRequest(request)).thenReturn(false);
        when(request.getContextPath()).thenReturn("");
        urlFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect(anyString());
    }
}