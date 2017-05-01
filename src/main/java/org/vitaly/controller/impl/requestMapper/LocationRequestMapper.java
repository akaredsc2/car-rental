package org.vitaly.controller.impl.requestMapper;

import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.util.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Properties;

import static org.vitaly.util.constants.RequestParameters.*;

/**
 * Created by vitaly on 28.04.17.
 */
public class LocationRequestMapper implements RequestMapper<LocationDto> {

    @Override
    public LocationDto map(HttpServletRequest request) {
        Properties properties = PropertyUtils.readProperties(PARAMETERS);

        long id = PropertyUtils.getLongFromRequest(request, properties, PARAM_LOCATION_ID);
        String photoUrl = request.getParameter(properties.getProperty(PARAM_LOCATION_PHOTO));
        String state = request.getParameter(properties.getProperty(PARAM_LOCATION_STATE));
        String city = request.getParameter(properties.getProperty(PARAM_LOCATION_CITY));
        String street = request.getParameter(properties.getProperty(PARAM_LOCATION_STREET));
        String building = request.getParameter(properties.getProperty(PARAM_LOCATION_BUILDING));

        LocationDto locationDto = new LocationDto();
        locationDto.setId(id);
        locationDto.setPhotoUrl(photoUrl);
        locationDto.setState(state);
        locationDto.setCity(city);
        locationDto.setStreet(street);
        locationDto.setBuilding(building);
        locationDto.setCarDtoList(Collections.emptyList());

        return locationDto;
    }
}
