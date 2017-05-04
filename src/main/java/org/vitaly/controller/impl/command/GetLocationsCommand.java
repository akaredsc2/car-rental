package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.vitaly.util.constants.RequestAttributes.ATTR_LOCATION_LIST;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_CAR_ID;

/**
 * Created by vitaly on 01.05.17.
 */
public class GetLocationsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        String carIdParam = properties.getProperty(PARAM_CAR_ID);

        List<LocationDto> locationDtoList;
        LocationService locationService = ServiceFactory.getInstance().getLocationService();
        if (parameterMap.containsKey(carIdParam)) {
            CarDto carDto = RequestMapperFactory.getInstance()
                    .getCarRequestMapper()
                    .map(request);

            locationDtoList = locationService.findLocationOfCar(carDto)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else {
            locationDtoList = locationService.getAll();
        }

        request.setAttribute(ATTR_LOCATION_LIST, locationDtoList);

        request.getServletContext().getRequestDispatcher("/pages/catalog/locations.jsp").forward(request, response);
    }
}
