package org.vitaly.service.impl.factory;

import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.location.Location;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.*;
import org.vitaly.service.impl.dtoMapper.*;

/**
 * Created by vitaly on 23.04.17.
 */
public class DtoMapperFactory {
    private static DtoMapperFactory instance = new DtoMapperFactory();

    private DtoMapper<Bill, BillDto> billDtoMapper = new BillDtoMapper();
    private DtoMapper<CarModel, CarModelDto> carModelDtoMapper = new CarModelDtoMapper();
    private DtoMapper<Car, CarDto> carDtoMapper = new CarDtoMapper();
    private DtoMapper<Location, LocationDto> locationDtoMapper = new LocationDtoMapper();
    private DtoMapper<Notification, NotificationDto> notificationDtoMapper = new NotificationDtoMapper();
    private DtoMapper<Reservation, ReservationDto> reservationDtoMapper = new ReservationDtoMapper();
    private DtoMapper<User, UserDto> userDtoMapper = new UserDtoMapper();

    private DtoMapperFactory() {
    }

    public static DtoMapperFactory getInstance() {
        return instance;
    }

    public DtoMapper<Bill, BillDto> getBillDtoMapper() {
        return billDtoMapper;
    }

    public DtoMapper<CarModel, CarModelDto> getCarModelDtoMapper() {
        return carModelDtoMapper;
    }

    public DtoMapper<Car, CarDto> getCarDtoMapper() {
        return carDtoMapper;
    }

    public DtoMapper<Location, LocationDto> getLocationDtoMapper() {
        return locationDtoMapper;
    }

    public DtoMapper<Notification, NotificationDto> getNotificationDtoMapper() {
        return notificationDtoMapper;
    }

    public DtoMapper<Reservation, ReservationDto> getReservationDtoMapper() {
        return reservationDtoMapper;
    }

    public DtoMapper<User, UserDto> getUserDtoMapper() {
        return userDtoMapper;
    }
}