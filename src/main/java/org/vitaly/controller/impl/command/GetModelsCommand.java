package org.vitaly.controller.impl.command;

import org.vitaly.controller.abstraction.command.Command;
import org.vitaly.controller.impl.factory.RequestMapperFactory;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
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

import static org.vitaly.util.constants.Pages.MODELS_JSP;
import static org.vitaly.util.constants.RequestAttributes.ATTR_MODEL_LIST;
import static org.vitaly.util.constants.RequestParameters.PARAMETERS;
import static org.vitaly.util.constants.RequestParameters.PARAM_CAR_ID;

/**
 * Created by vitaly on 01.05.17.
 */
public class GetModelsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Properties properties = PropertyUtils.readProperties(PARAMETERS);
        String carIdParam = properties.getProperty(PARAM_CAR_ID);

        List<CarModelDto> carModelDtoList;

        CarModelService carModelService = ServiceFactory.getInstance()
                .getCarModelService();

        if (parameterMap.containsKey(carIdParam)) {
            CarDto carDto = RequestMapperFactory.getInstance()
                    .getCarRequestMapper()
                    .map(request);

            carModelDtoList = carModelService.findModelOfCar(carDto)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else {
            carModelDtoList = carModelService
                    .getAll();
        }

        request.setAttribute(ATTR_MODEL_LIST, carModelDtoList);

        request.getServletContext()
                .getRequestDispatcher(MODELS_JSP)
                .forward(request, response);
    }
}
