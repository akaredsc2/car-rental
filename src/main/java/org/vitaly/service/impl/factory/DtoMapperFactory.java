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

    private DtoMapper<Bill, BillDto> billDtoMapper;
    private DtoMapper<CarModel, CarModelDto> carModelDtoMapper;
    private DtoMapper<Car, CarDto> carDtoMapper;
    private DtoMapper<Location, LocationDto> locationDtoMapper;
    private DtoMapper<Notification, NotificationDto> notificationDtoMapper;
    private DtoMapper<Reservation, ReservationDto> reservationDtoMapper;
    private DtoMapper<User, UserDto> userDtoMapper;

    public DtoMapperFactory() {
        this.billDtoMapper = new BillDtoMapper();
        this.carModelDtoMapper = new CarModelDtoMapper();
        this.carDtoMapper = new CarDtoMapper();
        this.locationDtoMapper = new LocationDtoMapper();
        this.notificationDtoMapper = new NotificationDtoMapper();
        this.reservationDtoMapper = new ReservationDtoMapper();
        this.userDtoMapper = new UserDtoMapper();
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