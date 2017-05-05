package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;
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
public class MoveCarCommandTest {

    @Mock
    private RequestMapper<CarDto> carRequestMapper;

    @Mock
    private RequestMapper<LocationDto> locationRequestMapper;

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

    private MoveCarCommand moveCarCommand = new MoveCarCommand();

    @Test
    public void successfulMovingCarSendRedirect() throws Exception {
        CarDto carDto = new CarDto();
        LocationDto locationDto = new LocationDto();

        when(carRequestMapper.map(request)).thenReturn(carDto);
        when(locationRequestMapper.map(request)).thenReturn(locationDto);
        when(carService.moveCarToLocation(any(), any())).thenReturn(true);
        moveCarCommand.execute(request, response);

        verify(response).sendRedirect(contains(HOME_JSP));
    }

    @Test
    public void failedMovingCarForwardsToErrorPage() throws Exception {
        CarDto carDto = new CarDto();
        LocationDto locationDto = new LocationDto();

        when(carRequestMapper.map(request)).thenReturn(carDto);
        when(locationRequestMapper.map(request)).thenReturn(locationDto);
        when(carService.moveCarToLocation(any(), any())).thenReturn(false);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(contains(ERROR_JSP))).thenReturn(requestDispatcher);
        moveCarCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}