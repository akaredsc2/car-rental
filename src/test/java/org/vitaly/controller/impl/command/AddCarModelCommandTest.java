package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.abstraction.validation.Validator;
import org.vitaly.controller.impl.command.carModel.AddCarModelCommand;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.controller.impl.factory.ValidatorFactory;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.controller.impl.validation.AddModelValidator;
import org.vitaly.controller.impl.validation.ValidationResultImpl;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
public class AddCarModelCommandTest {

    @Mock
    private RequestMapper<CarModelDto> carModelRequestMapper;

    @InjectMocks
    private RequestMapperFactory requestMapperFactory = RequestMapperFactory.getInstance();

    @Mock
    private CarModelService carModelService;

    @InjectMocks
    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    @Mock
    private AddModelValidator addModelValidator;

    @InjectMocks
    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();

    @Mock
    private HttpServletRequest request;

    @Mock
    private Part part;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private RequestDispatcher requestDispatcher;

    private AddCarModelCommand addCarModelCommand = new AddCarModelCommand();

    @Test
    public void successfulAddingCarModelSendRedirect() throws Exception {
        CarModelDto carModelDto = new CarModelDto();

        when(request.getPart(any())).thenReturn(part);
        when(addModelValidator.validate(any())).thenReturn(new ValidationResultImpl());
        when(carModelRequestMapper.map(request)).thenReturn(carModelDto);
        when(carModelService.addCarModel(any())).thenReturn(true);
        addCarModelCommand.execute(request, response);

        verify(response).sendRedirect(contains(HOME_JSP));
    }

    @Test
    public void failedAddingCarModelForwardsToErrorPage() throws Exception {
        CarModelDto carModelDto = new CarModelDto();

        when(addModelValidator.validate(any())).thenReturn(new ValidationResultImpl());
        when(request.getPart(any())).thenReturn(part);
        when(carModelRequestMapper.map(request)).thenReturn(carModelDto);
        when(carModelService.addCarModel(any())).thenReturn(false);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(contains(ERROR_JSP))).thenReturn(requestDispatcher);
        addCarModelCommand.execute(request, response);

        verify(request).setAttribute(eq(ATTR_ERROR), anyString());
        verify(requestDispatcher).forward(request, response);
    }
}