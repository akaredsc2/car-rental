package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.vitaly.util.constants.RequestAttributes.ATTR_CAR_ID;
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

        // TODO: 01.05.17 replace with find car by id
        long carId = PropertyUtils.getLongFromRequest(request, properties, PARAM_CAR_ID);
        List<LocationDto> locationDtoList = ServiceFactory.getInstance()
                .getLocationService()
                .getAll();

        request.setAttribute(ATTR_CAR_ID, carId);
        request.setAttribute(ATTR_LOCATION_LIST, locationDtoList);

        request.getServletContext().getRequestDispatcher("/pages/admin/move_car.jsp").forward(request, response);
    }
}
