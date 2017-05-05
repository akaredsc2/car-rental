package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
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
import static org.vitaly.util.constants.Pages.MODELS_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_MODEL_LIST;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetModelsCommandTest {

    @Mock
    private RequestMapper<CarDto> carRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private CarModelService carModelService;

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

    private GetModelsCommand getModelsCommand = new GetModelsCommand();

    @Test
    public void supplyingIdOfExistingCarAsRequestParameterWillFetchModelOfThisCar() throws Exception {
        CarDto carDto = new CarDto();
        CarModelDto carModelDto = new CarModelDto();

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("param_car_id", new String[]{}));
        when(carRequestMapper.map(any())).thenReturn(carDto);
        when(carModelService.findModelOfCar(any())).thenReturn(Optional.of(carModelDto));
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(MODELS_JSP)).thenReturn(requestDispatcher);
        getModelsCommand.execute(request, response);

        verify(carModelService).findModelOfCar(any());
        verify(request).setAttribute(eq(ATTR_MODEL_LIST), argThat(hasSize(1)));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void supplyingIdOfNonExistingCarAsRequestParameterWillFetchNothing() throws Exception {
        CarDto carDto = new CarDto();

        when(request.getParameterMap()).thenReturn(Collections.singletonMap("param_car_id", new String[]{}));
        when(carRequestMapper.map(any())).thenReturn(carDto);
        when(carModelService.findModelOfCar(any())).thenReturn(Optional.empty());
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(MODELS_JSP)).thenReturn(requestDispatcher);
        getModelsCommand.execute(request, response);

        verify(carModelService).findModelOfCar(any());
        verify(request).setAttribute(eq(ATTR_MODEL_LIST), argThat(empty()));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void notSupplyingCarIdInRequestWillFetchAllModels() throws Exception {
        CarDto carDto = new CarDto();

        when(carRequestMapper.map(any())).thenReturn(carDto);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(MODELS_JSP)).thenReturn(requestDispatcher);
        getModelsCommand.execute(request, response);

        verify(carModelService).getAll();
        verify(request).setAttribute(eq(ATTR_MODEL_LIST), any());
        verify(requestDispatcher).forward(request, response);
    }
}