package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.command.user.RegistrationCommand;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.controller.impl.validation.ChangePasswordValidator;
import org.vitaly.controller.impl.validation.RegistrationValidator;
import org.vitaly.controller.impl.validation.ValidationResultImpl;
import org.vitaly.service.abstraction.UserService;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.INDEX_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegistrationCommandTest {

    @Mock
    private RequestMapper<UserDto> userRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private RegistrationValidator registrationValidator;

    @InjectMocks
    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();

    @Mock
    private UserService userService;

    @InjectMocks
    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    private RegistrationCommand registrationCommand = new RegistrationCommand();

    @Test
    public void successfulRegistrationSendRedirect() throws Exception {
        UserDto userDto = new UserDto();

        when(userRequestMapper.map(request)).thenReturn(userDto);
        when(registrationValidator.validate(any())).thenReturn(new ValidationResultImpl());
        when(userService.registerNewUser(any())).thenReturn(true);
        registrationCommand.execute(request, response);

        verify(response).sendRedirect(contains(INDEX_JSP));
    }

    @Test
    public void failedRegistrationForwardsToErrorPage() throws Exception {
        UserDto userDto = new UserDto();

        when(userRequestMapper.map(request)).thenReturn(userDto);
        when(userService.registerNewUser(any())).thenReturn(false);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(contains(ERROR_JSP))).thenReturn(requestDispatcher);
        when(registrationValidator.validate(any())).thenReturn(new ValidationResultImpl());
        registrationCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}