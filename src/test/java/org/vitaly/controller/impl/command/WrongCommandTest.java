package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class WrongCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    private WrongCommand wrongCommand = new WrongCommand();

    @Test
    public void wrongCommandRedirectsToErrorPage() throws Exception {
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(eq(ERROR_JSP))).thenReturn(requestDispatcher);
        wrongCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}