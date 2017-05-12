package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.controller.impl.command.car.AddCarCommand;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.controller.impl.validation.AddCarValidator;
import org.vitaly.controller.impl.validation.ValidationResultImpl;
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
 * Created by vitaly on 2017-05-04.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddCarCommandTest {

    @Mock
    private RequestMapper<CarDto> carRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private CarService carService;

    @InjectMocks
    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    @Mock
    private AddCarValidator addCarValidator;

    @InjectMocks
    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    private AddCarCommand addCarCommand = new AddCarCommand();

    @Test
    public void successfulAddingCarSendRedirect() throws Exception {
        CarDto carDto = new CarDto();

        when(addCarValidator.validate(any())).thenReturn(new ValidationResultImpl());
        when(carRequestMapper.map(request)).thenReturn(carDto);
        when(carService.addNewCar(any())).thenReturn(true);
        addCarCommand.execute(request, response);

        verify(response).sendRedirect(contains(HOME_JSP));
    }

    @Test
    public void failedAddingCarForwardsToErrorPage() throws Exception {
        CarDto carDto = new CarDto();

        when(addCarValidator.validate(any())).thenReturn(new ValidationResultImpl());
        when(carRequestMapper.map(request)).thenReturn(carDto);
        when(carService.addNewCar(any())).thenReturn(false);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(contains(ERROR_JSP))).thenReturn(requestDispatcher);
        addCarCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}