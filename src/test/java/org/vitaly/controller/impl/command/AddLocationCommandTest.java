package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.service.abstraction.LocationService;
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
public class AddLocationCommandTest {

    @Mock
    private RequestMapper<LocationDto> locationRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private LocationService locationService;

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

    private AddLocationCommand addLocationCommand = new AddLocationCommand();

    @Test
    public void successfulAddingLocationSendRedirect() throws Exception {
        LocationDto locationDto = new LocationDto();

        when(locationRequestMapper.map(request)).thenReturn(locationDto);
        when(locationService.addNewLocation(any())).thenReturn(true);
        addLocationCommand.execute(request, response);

        verify(response).sendRedirect(contains(HOME_JSP));
    }

    @Test
    public void failedAddingLocationForwardsToErrorPage() throws Exception {
        LocationDto locationDto = new LocationDto();

        when(locationRequestMapper.map(request)).thenReturn(locationDto);
        when(locationService.addNewLocation(any())).thenReturn(false);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(contains(ERROR_JSP))).thenReturn(requestDispatcher);
        addLocationCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}