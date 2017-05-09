package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.command.car.ChangeCarStateCommand;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.ERROR_JSP;
import static org.vitaly.util.constants.Pages.HOME_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_ERROR;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChangeCarStateCommandTest {

    @Mock
    private RequestMapper<CarDto> carRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private CarService carService;

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

    private ChangeCarStateCommand changeCarStateCommand = new ChangeCarStateCommand();

    @Test
    public void successfulChangingCarStateSendRedirect() throws Exception {
        CarDto carDto = new CarDto();
        String correctCarState = "available";

        when(request.getParameter(contains("state"))).thenReturn(correctCarState);
        when(carRequestMapper.map(request)).thenReturn(carDto);
        when(carService.changeCarState(any(), eq(correctCarState))).thenReturn(true);
        changeCarStateCommand.execute(request, response);

        verify(response).sendRedirect(contains(HOME_JSP));
    }

    @Test
    public void failedChangingCarStateForwardsToErrorPage() throws Exception {
        CarDto carDto = new CarDto();
        String wrongCarState = "random";

        when(request.getParameter(contains("state"))).thenReturn(wrongCarState);
        when(carRequestMapper.map(request)).thenReturn(carDto);
        when(carService.changeCarState(any(), eq(wrongCarState))).thenReturn(false);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(contains(ERROR_JSP))).thenReturn(requestDispatcher);
        changeCarStateCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}