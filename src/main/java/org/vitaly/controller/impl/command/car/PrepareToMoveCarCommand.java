package org.vitaly.controller.impl.command.car;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.vitaly.util.constants.Pages.CARS_JSP;
import static org.vitaly.util.constants.Pages.MOVE_CAR_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_CAR;
import static org.vitaly.util.constants.RequestAttributes.ATTR_LOCATION_LIST;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_CAR_ID;

/**
 * Created by vitaly on 01.05.17.
 */
public class PrepareToMoveCarCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        long carId = PropertyUtils.getLongFromRequest(request, properties, PARAM_CAR_ID);

        CarDto carDto = ServiceFactory.getInstance()
                .getCarService()
                .findCarById(carId)
                .orElseGet(CarDto::new);

        List<LocationDto> locationDtoList = ServiceFactory.getInstance()
                .getLocationService()
                .getAll();

        request.setAttribute(ATTR_CAR, carDto);
        request.setAttribute(ATTR_LOCATION_LIST, locationDtoList);

        request.getServletContext()
                .getRequestDispatcher(MOVE_CAR_JSP)
                .forward(request, response);
    }
}
