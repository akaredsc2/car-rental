package org.vitaly.security;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-30.
 */
public class UrlHttpMethodPairTest {

    @Test
    public void fromRequestCorrectlyTakesData() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String method = "POST";
        String contextPath = "path";
        String action = "/uri";
        String uri = contextPath + action;

        when(request.getMethod()).thenReturn(method);
        when(request.getContextPath()).thenReturn(contextPath);
        when(request.getRequestURI()).thenReturn(uri);
        UrlHttpMethodPair actual = UrlHttpMethodPair.fromRequest(request);

        UrlHttpMethodPair expected = new UrlHttpMethodPair(action, HttpMethod.valueOf(method));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromNullRequestShouldThrowException() throws Exception {
        UrlHttpMethodPair.fromRequest(null);
    }
}