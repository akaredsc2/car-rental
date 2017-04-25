package org.vitaly.dao.impl.mysql.factory;

import org.vitaly.dao.impl.mysql.mapper.*;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.location.Location;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;

/**
 * Created by vitaly on 18.04.17.
 */
public class ResultSetMapperFactory {
    private static ResultSetMapperFactory instance = new ResultSetMapperFactory();

    private Mapper<Bill> billMapper;
    private Mapper<CarModel> carModelMapper;
    private Mapper<Car> carMapper;
    private Mapper<Location> locationMapper;
    private Mapper<Notification> notificationMapper;
    private Mapper<Reservation> reservationMapper;
    private Mapper<User> userMapper;

    private ResultSetMapperFactory() {
        this.billMapper = new BillMapper();
        this.carModelMapper = new CarModelMapper();
        this.carMapper = new CarMapper();
        this.locationMapper = new LocationMapper();
        this.notificationMapper = new NotificationMapper();
        this.reservationMapper = new ReservationMapper();
        this.userMapper = new UserMapper();
    }

    public static ResultSetMapperFactory getInstance() {
        return instance;
    }

    public Mapper<Bill> getBillMapper() {
        return billMapper;
    }

    public Mapper<CarModel> getCarModelMapper() {
        return carModelMapper;
    }

    public Mapper<Car> getCarMapper() {
        return carMapper;
    }

    public Mapper<Location> getLocationMapper() {
        return locationMapper;
    }

    public Mapper<Notification> getNotificationMapper() {
        return notificationMapper;
    }

    public Mapper<Reservation> getReservationMapper() {
        return reservationMapper;
    }

    public Mapper<User> getUserMapper() {
        return userMapper;
    }
}
