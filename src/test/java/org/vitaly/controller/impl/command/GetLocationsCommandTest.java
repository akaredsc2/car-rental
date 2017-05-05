package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.LOCATIONS_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_LOCATION_LIST;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetLocationsCommandTest {

    @Mock
    private RequestMapper<CarDto> carRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private LocationService locationService;

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

    private GetLocationsCommand getLocationsCommand = new GetLocationsCommand();

    @Test
    public void supplyingIdOfExistingCarAsRequestParameterWillFetchLocationOfThisCar() throws Exception {
        CarDto carDto = new CarDto();
        LocationDto locationDto = new LocationDto();

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("param_car_id", new String[]{}));
        when(carRequestMapper.map(any())).thenReturn(carDto);
        when(locationService.findLocationOfCar(any())).thenReturn(Optional.of(locationDto));
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(LOCATIONS_JSP)).thenReturn(requestDispatcher);
        getLocationsCommand.execute(request, response);

        verify(locationService).findLocationOfCar(any());
        verify(request).setAttribute(eq(ATTR_LOCATION_LIST), argThat(hasSize(1)));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void supplyingIdOfNonExistingCarAsRequestParameterWillFetchNothing() throws Exception {
        CarDto carDto = new CarDto();

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("param_car_id", new String[]{}));
        when(carRequestMapper.map(any())).thenReturn(carDto);
        when(locationService.findLocationOfCar(any())).thenReturn(Optional.empty());
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(LOCATIONS_JSP)).thenReturn(requestDispatcher);
        getLocationsCommand.execute(request, response);

        verify(locationService).findLocationOfCar(any());
        verify(request).setAttribute(eq(ATTR_LOCATION_LIST), argThat(empty()));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void notSupplyingCarIdInRequestWillFetchAllLocations() throws Exception {
        CarDto carDto = new CarDto();

        when(carRequestMapper.map(any())).thenReturn(carDto);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(LOCATIONS_JSP)).thenReturn(requestDispatcher);
        getLocationsCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_LOCATION_LIST), any());
        verify(locationService).getAll();
        verify(requestDispatcher).forward(request, response);
    }
}