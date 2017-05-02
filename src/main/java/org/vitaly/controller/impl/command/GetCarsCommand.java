package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.factory.ServiceFactory;
import org.vitaly.util.PropertyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.vitaly.util.constants.RequestAttributes.ATTR_CAR_LIST;
import static org.vitaly.util.constants.RequestParameters.*;

/**
 * Created by vitaly on 01.05.17.
 */
public class GetCarsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CarService carService = ServiceFactory.getInstance().getCarService();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        String locationIdParam = properties.getProperty(PARAM_LOCATION_ID);
        String modelIdParam = properties.getProperty(PARAM_MODEL_ID);

        List<CarDto> carDtoList;
        if (parameterMap.containsKey(locationIdParam)) {
            LocationDto locationDto = RequestMapperFactory.getInstance()
                    .getLocationRequestMapper()
                    .map(request);

            carDtoList = carService.findCarsAtLocation(locationDto);
        } else if (parameterMap.containsKey(modelIdParam)) {
            CarModelDto carModelDto = RequestMapperFactory.getInstance()
                    .getCarModelRequestMapper()
                    .map(request);

            carDtoList = carService.findCarsByModel(carModelDto);
        } else {
            carDtoList = carService.getAllCars();
        }

        request.setAttribute(ATTR_CAR_LIST, carDtoList);

        request.getServletContext()
                .getRequestDispatcher("/pages/catalog/cars.jsp")
                .forward(request, response);
    }
}
