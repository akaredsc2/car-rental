package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.command.user.PrepareToPromoteCommand;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.PROMOTE_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_USER_LIST;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class PrepareToPromoteCommandTest {
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private ServiceFactory serviceFactory = ServiceFactory.getInstance();
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private ServletContext servletContext;
    
    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private HttpServletResponse response;

    private PrepareToPromoteCommand prepareToPromoteCommand = new PrepareToPromoteCommand();

    @Test
    public void prepareToPromoteFetchesAllClientsAndForwardsToPromotionPage() throws Exception {
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(eq(PROMOTE_JSP))).thenReturn(requestDispatcher);

        prepareToPromoteCommand.execute(request, response);

        verify(userService).findAllClients();
        verify(request).setAttribute(eq(ATTR_USER_LIST), anyList());
        verify(requestDispatcher).forward(request, response);
    }
}