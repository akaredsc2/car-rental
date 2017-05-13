package org.vitaly.controller.impl.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.controller.impl.command.car.PrepareToMoveCarCommand;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.vitaly.util.constants.Pages.MOVE_CAR_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_CAR;
import static org.vitaly.util.constants.RequestAttributes.ATTR_LOCATION_LIST;

/**
 * Created by vitaly on 2017-05-05.
 */
@RunWith(MockitoJUnitRunner.class)
public class PrepareToMoveCarCommandTest {

    @Mock
    private LocationService locationService;

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

    private PrepareToMoveCarCommand prepareToMoveCarCommand = new PrepareToMoveCarCommand();

    @Test
    public void prepareToMoveCarFetchesAllLocationsAndForwardsToPromotionPage() throws Exception {
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(eq(MOVE_CAR_JSP))).thenReturn(requestDispatcher);
        when(carService.findCarById(anyLong())).thenReturn(Optional.of(new CarDto()));

        prepareToMoveCarCommand.execute(request, response);

        verify(locationService).getAll();
        verify(request).setAttribute(eq(ATTR_CAR), any());
        verify(request).setAttribute(eq(ATTR_LOCATION_LIST), anyList());
        verify(requestDispatcher).forward(request, response);
    }
}