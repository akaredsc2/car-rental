package org.vitaly.controller.impl.requestMapper;

import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;

/**
 * Created by vitaly on 28.04.17.
 */
public class CarModelRequestMapper implements RequestMapper<CarModelDto> {

    @Override
    public CarModelDto map(HttpServletRequest request) {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        long id = PropertyUtils.getLongFromRequest(request, properties, PARAM_MODEL_ID);

        String name = request.getParameter(properties.getProperty(PARAM_MODEL_NAME));
        String photoUrl = request.getParameter(properties.getProperty(PARAM_MODEL_PHOTO));

        int doorCount = PropertyUtils.getIntFromRequest(request, properties, PARAM_MODEL_DOOR);
        int seatCount = PropertyUtils.getIntFromRequest(request, properties, PARAM_MODEL_SEAT);
        int horsePowerCount = PropertyUtils.getIntFromRequest(request, properties, PARAM_MODEL_HORSE);

        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(id);
        carModelDto.setName(name);
        carModelDto.setPhotoUrl(photoUrl);
        carModelDto.setDoorCount(doorCount);
        carModelDto.setSeatCount(seatCount);
        carModelDto.setHorsePowerCount(horsePowerCount);

        return carModelDto;
    }
}
