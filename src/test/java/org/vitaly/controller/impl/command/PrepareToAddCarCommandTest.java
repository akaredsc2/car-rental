package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.ADD_CAR_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_MODEL_LIST;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class PrepareToAddCarCommandTest {

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

    private PrepareToAddCarCommand prepareToAddCarCommand = new PrepareToAddCarCommand();

    @Test
    public void prepareToAddCarFetchesAllCarModelsAndForwardsToPromotionPage() throws Exception {
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(eq(ADD_CAR_JSP))).thenReturn(requestDispatcher);

        prepareToAddCarCommand.execute(request, response);

        verify(carModelService).getAll();
        verify(request).setAttribute(eq(ATTR_MODEL_LIST), anyList());
        verify(requestDispatcher).forward(request, response);
    }
}