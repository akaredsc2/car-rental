package org.vitaly.controller.impl.factory;

import org.vitaly.controller.impl.requestMapper.CarModelRequestMapper;
import org.vitaly.controller.impl.requestMapper.RequestMapper;
import org.vitaly.controller.impl.requestMapper.UserRequestMapper;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.UserDto;

/**
 * Created by vitaly on 28.04.17.
 */
public class RequestMapperFactory {
    private static RequestMapperFactory instance = new RequestMapperFactory();

    private RequestMapper<UserDto> userRequestMapper = new UserRequestMapper();
    private RequestMapper<CarModelDto> carModelRequestMapper = new CarModelRequestMapper();

    public static RequestMapperFactory getInstance() {
        return instance;
    }

    public RequestMapper<UserDto> getUserRequestMapper() {
        return userRequestMapper;
    }

    public RequestMapper<CarModelDto> getCarModelRequestMapper() {
        return carModelRequestMapper;
    }
}
