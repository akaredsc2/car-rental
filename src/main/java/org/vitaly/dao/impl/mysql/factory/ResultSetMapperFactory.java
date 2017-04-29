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

    private Mapper<Bill> billMapper = new BillMapper();
    private Mapper<CarModel> carModelMapper = new CarModelMapper();
    private Mapper<Car> carMapper = new CarMapper();
    private Mapper<Location> locationMapper = new LocationMapper();
    private Mapper<Notification> notificationMapper = new NotificationMapper();
    private Mapper<Reservation> reservationMapper = new ReservationMapper();
    private Mapper<User> userMapper = new UserMapper();

    private ResultSetMapperFactory() {
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
