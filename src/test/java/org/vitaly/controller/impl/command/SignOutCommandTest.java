package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.INDEX_JSP;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class SignOutCommandTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    private SignOutCommand signOutCommand = new SignOutCommand();

    @Test
    public void signOutInvalidatesSessionAndRedirectsToIndex() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        signOutCommand.execute(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect(contains(INDEX_JSP));
    }
}