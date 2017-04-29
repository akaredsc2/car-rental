package org.vitaly.controller.impl.requestMapper;

import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Properties;

import static org.vitaly.util.PropertyNames.*;

/**
 * Created by vitaly on 29.04.17.
 */
public class CarRequestMapper implements RequestMapper<CarDto> {

    @Override
    public CarDto map(HttpServletRequest request) {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        long id = PropertyUtils.getLongFromRequest(request, properties, PARAM_CAR_ID);

        String stateString = request.getParameter(properties.getProperty(PARAM_CAR_STATE));
        CarState state = CarStateEnum
                .getStateByName(stateString)
                .orElse(null);

        long modelId = PropertyUtils.getLongFromRequest(request, properties, PARAM_CAR_MODEL);
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(modelId);

        String registrationPlate = request.getParameter(properties.getProperty(PARAM_CAR_PLATE));
        String color = request.getParameter(properties.getProperty(PARAM_CAR_COLOR));

        BigDecimal pricePerDay = PropertyUtils.getBigDecimalFromRequest(request, properties, PARAM_CAR_PRICE);

        CarDto carDto = new CarDto();
        carDto.setId(id);
        carDto.setState(state);
        carDto.setCarModelDto(carModelDto);
        carDto.setRegistrationPlate(registrationPlate);
        carDto.setColor(color);
        carDto.setPricePerDay(pricePerDay);

        return carDto;
    }
}
