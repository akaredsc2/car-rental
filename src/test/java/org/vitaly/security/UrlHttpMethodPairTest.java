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
    public void fromRequestDiscardsEverythingExceptActionIfUriIsNotEndingWithJsp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String method = "POST";
        String contextPath = "contextPath";
        String path = "/path";
        String action = "/controller/action";
        String uri = contextPath + path + action;

        when(request.getMethod()).thenReturn(method);
        when(request.getContextPath()).thenReturn(contextPath);
        when(request.getRequestURI()).thenReturn(uri);
        UrlHttpMethodPair actual = UrlHttpMethodPair.fromRequest(request);

        UrlHttpMethodPair expected = new UrlHttpMethodPair(action, HttpMethod.valueOf(method));
        assertEquals(expected, actual);
    }

    @Test
    public void fromRequestDiscardsContextPathIfUriIsEndingWithJsp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String method = "POST";
        String contextPath = "contextPath";
        String path = "/path";
        String page = "/page.jsp";
        String uri = contextPath + path + page;

        when(request.getMethod()).thenReturn(method);
        when(request.getContextPath()).thenReturn(contextPath);
        when(request.getRequestURI()).thenReturn(uri);
        UrlHttpMethodPair actual = UrlHttpMethodPair.fromRequest(request);

        UrlHttpMethodPair expected = new UrlHttpMethodPair(path + page, HttpMethod.valueOf(method));
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromNullRequestShouldThrowException() throws Exception {
        UrlHttpMethodPair.fromRequest(null);
    }
}