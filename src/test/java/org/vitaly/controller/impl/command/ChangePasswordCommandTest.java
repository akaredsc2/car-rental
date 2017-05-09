package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.command.user.ChangePasswordCommand;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.constants.RequestParameters;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;
import static org.vitaly.util.constants.SessionAttributes.SESSION_USER;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangePasswordCommandTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    private ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand();

    @Test
    public void successfulChangingPasswordUpdatesUserInSessionAndSendRedirect() throws Exception {
        UserDto userDto = new UserDto();
        String newPassword = "new password";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_USER)).thenReturn(userDto);
        when(request.getParameter(eq(RequestParameters.PARAM_PASS_NEW))).thenReturn(newPassword);
        when(userService.changePassword(any(), anyString())).thenReturn(true);
        changePasswordCommand.execute(request, response);

        verify(session).setAttribute(eq(SESSION_USER), any());
        verify(response).sendRedirect(contains(HOME_JSP));
    }

    @Test
    public void failedChangingPasswordForwardsToErrorPage() throws Exception {
        UserDto userDto = new UserDto();
        String newPassword = "bad password";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_USER)).thenReturn(userDto);
        when(request.getParameter(eq(RequestParameters.PARAM_PASS_NEW))).thenReturn(newPassword);
        when(userService.changePassword(any(), anyString())).thenReturn(false);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(eq(ERROR_JSP))).thenReturn(requestDispatcher);
        changePasswordCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}