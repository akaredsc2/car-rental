package org.vitaly.security;

import org.junit.Test;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 2017-04-30.
 */
public class SecurityManagerTest {

    @Test
    public void validRequestReturnsTrueOnValidation() throws Exception {
        UrlHttpMethodPair validUrl = UrlHttpMethodPair.ADD_CAR_GET;

        HttpServletRequest validRequest = stabRequest(validUrl);
        boolean isValidRequest = SecurityManager.getInstance().isValidRequest(validRequest);

        assertTrue(isValidRequest);
    }

    private HttpServletRequest stabRequest(UrlHttpMethodPair urlHttpMethodPair) {
        String contextPath = "path";
        String action = urlHttpMethodPair.getUrl();
        String url = contextPath + action;
        HttpMethod method = urlHttpMethodPair.getHttpMethod();
        HttpServletRequest validRequest = mock(HttpServletRequest.class);

        when(validRequest.getMethod()).thenReturn(method.toString());
        when(validRequest.getContextPath()).thenReturn(contextPath);
        when(validRequest.getRequestURI()).thenReturn(url);
        return validRequest;
    }

    @Test
    public void invalidRequestReturnsFalseOnValidation() throws Exception {
        UrlHttpMethodPair invalidUrl = new UrlHttpMethodPair("/random", HttpMethod.GET);

        HttpServletRequest invalidRequest = stabRequest(invalidUrl);
        boolean isValidRequest = SecurityManager.getInstance().isValidRequest(invalidRequest);

        assertFalse(isValidRequest);
    }

    @Test
    public void guestAllowedRequestIsAllowedWithEmptySession() throws Exception {
        UrlHttpMethodPair guestOnlyUrl = UrlHttpMethodPair.SIGN_IN;

        HttpServletRequest request = stabRequest(guestOnlyUrl);
        when(request.getSession(anyBoolean())).thenReturn(null);
        boolean isRequestAllowed = SecurityManager.getInstance().isRequestAllowed(request);

        assertTrue(isRequestAllowed);
    }

    @Test
    public void guestAllowedRequestIsAllowedWithEmptyUserAttributeInSession() throws Exception {
        UrlHttpMethodPair guestOnlyUrl = UrlHttpMethodPair.SIGN_IN;

        HttpServletRequest request = stabRequest(guestOnlyUrl);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(anyBoolean())).thenReturn(null);
        when(session.getAttribute(any())).thenReturn(null);
        boolean isRequestAllowed = SecurityManager.getInstance().isRequestAllowed(request);

        assertTrue(isRequestAllowed);
    }

    @Test
    public void guestDisallowedRequestIsNotAllowedWithEmptySession() throws Exception {
        UrlHttpMethodPair adminOnlyUrl = UrlHttpMethodPair.ADD_CAR_POST;

        HttpServletRequest request = stabRequest(adminOnlyUrl);
        when(request.getSession(anyBoolean())).thenReturn(null);
        boolean isRequestAllowed = SecurityManager.getInstance().isRequestAllowed(request);

        assertFalse(isRequestAllowed);
    }

    @Test
    public void guestADisallowedRequestIsNotAllowedWithEmptyUserAttributeInSession() throws Exception {
        UrlHttpMethodPair adminOnlyUrl = UrlHttpMethodPair.ADD_CAR_POST;

        HttpServletRequest request = stabRequest(adminOnlyUrl);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute(SESSION_USER)).thenReturn(null);
        boolean isRequestAllowed = SecurityManager.getInstance().isRequestAllowed(request);

        assertFalse(isRequestAllowed);
    }

    @Test
    public void requestAllowedForRoleReturnsTrue() throws Exception {
        UrlHttpMethodPair adminOnlyUrl = UrlHttpMethodPair.ADD_LOCATION_POST;
        UserDto adminDto = new UserDto();
        adminDto.setRole(UserRole.ADMIN);

        HttpServletRequest request = stabRequest(adminOnlyUrl);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute(SESSION_USER)).thenReturn(adminDto);
        boolean isRequestAllowed = SecurityManager.getInstance().isRequestAllowed(request);

        assertTrue(isRequestAllowed);
    }

    @Test
    public void requestNotAllowedForRoleReturnsFalse() throws Exception {
        UrlHttpMethodPair guestOnlyUrl = UrlHttpMethodPair.REGISTRATION;
        UserDto adminDto = new UserDto();
        adminDto.setRole(UserRole.ADMIN);

        HttpServletRequest request = stabRequest(guestOnlyUrl);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute(SESSION_USER)).thenReturn(adminDto);
        boolean isRequestAllowed = SecurityManager.getInstance().isRequestAllowed(request);

        assertFalse(isRequestAllowed);
    }
}