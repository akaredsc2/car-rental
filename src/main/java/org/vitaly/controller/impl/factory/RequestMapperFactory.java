package org.vitaly.controller.impl.factory;

import org.vitaly.controller.impl.requestMapper.*;
import org.vitaly.service.impl.dto.*;

/**
 * Created by vitaly on 28.04.17.
 */
public class RequestMapperFactory {
    private static RequestMapperFactory instance = new RequestMapperFactory();

    private RequestMapper<UserDto> userRequestMapper = new UserRequestMapper();
    private RequestMapper<CarModelDto> carModelRequestMapper = new CarModelRequestMapper();
    private RequestMapper<LocationDto> locationRequestMapper = new LocationRequestMapper();
    private RequestMapper<CarDto> carRequestMapper = new CarRequestMapper();
    private RequestMapper<ReservationDto> reservationRequestMapper = new ReservationRequestMapper();
    private RequestMapper<BillDto> billRequestMapper = new BillRequestMapper();

    private RequestMapperFactory() {
    }

    public static RequestMapperFactory getInstance() {
        return instance;
    }

    public RequestMapper<UserDto> getUserRequestMapper() {
        return userRequestMapper;
    }

    public RequestMapper<CarModelDto> getCarModelRequestMapper() {
        return carModelRequestMapper;
    }

    public RequestMapper<LocationDto> getLocationRequestMapper() {
        return locationRequestMapper;
    }

    public RequestMapper<CarDto> getCarRequestMapper() {
        return carRequestMapper;
    }

    public RequestMapper<ReservationDto> getReservationRequestMapper() {
        return reservationRequestMapper;
    }

    public RequestMapper<BillDto> getBillRequestMapper() {
        return billRequestMapper;
    }
}
