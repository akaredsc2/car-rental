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
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.CARS_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_CAR_LIST;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCarsCommandTest {

    @Mock
    private RequestMapper<LocationDto> locationRequestMapper;

    @Mock
    private RequestMapper<CarModelDto> carModelRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private CarService carService;

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

    private GetCarsCommand getCarsCommand = new GetCarsCommand();

    @Test
    public void supplyingIdOfLocationAsRequestParameterWillFetchCarsAtThatLocation() throws Exception {
        List<CarDto> carDtoList = new ArrayList<>();
        LocationDto locationDto = new LocationDto();

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("param_location_id", new String[]{}));
        when(locationRequestMapper.map(any())).thenReturn(locationDto);
        when(carService.findCarsAtLocation(any())).thenReturn(carDtoList);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(CARS_JSP)).thenReturn(requestDispatcher);
        getCarsCommand.execute(request, response);

        verify(carService).findCarsAtLocation(any());
        verify(request).setAttribute(eq(ATTR_CAR_LIST), anyList());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void supplyingIdOfCarModelAsRequestParameterWillFetchCarsOfThatModel() throws Exception {
        List<CarDto> carDtoList = new ArrayList<>();
        CarModelDto carModelDto = new CarModelDto();

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("param_model_id", new String[]{}));
        when(carModelRequestMapper.map(any())).thenReturn(carModelDto);
        when(carService.findCarsByModel(any())).thenReturn(carDtoList);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(CARS_JSP)).thenReturn(requestDispatcher);
        getCarsCommand.execute(request, response);

        verify(carService).findCarsByModel(any());
        verify(request).setAttribute(eq(ATTR_CAR_LIST), anyList());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void notSupplyingCarModelIdOrLocationInRequestWillFetchAllCars() throws Exception {
        List<CarDto> carDtoList = new ArrayList<>();

        when(carService.getAllCars()).thenReturn(carDtoList);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(CARS_JSP)).thenReturn(requestDispatcher);
        getCarsCommand.execute(request, response);

        verify(carService).getAllCars();
        verify(request).setAttribute(eq(ATTR_CAR_LIST), anyList());
        verify(requestDispatcher).forward(request, response);
    }
}