package org.vitaly.dao.impl.mysql.factory;

import org.vitaly.dao.impl.mysql.mapper.*;
import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.location.Location;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;

/**
 * Factory for result set mappers
 */
public class ResultSetMapperFactory {
    private static ResultSetMapperFactory instance = new ResultSetMapperFactory();

    private Mapper<Bill> billMapper = new BillMapper();
    private Mapper<CarModel> carModelMapper = new CarModelMapper();
    private Mapper<Car> carMapper = new CarMapper();
    private Mapper<Location> locationMapper = new LocationMapper();
    private Mapper<Reservation> reservationMapper = new ReservationMapper();
    private Mapper<User> userMapper = new UserMapper();

    private ResultSetMapperFactory() {
    }

    /**
     * Instance of factory
     *
     * @return instance of factory
     */
    public static ResultSetMapperFactory getInstance() {
        return instance;
    }

    /**
     * Bill result set mapper
     *
     * @return bill result set mapper
     */
    public Mapper<Bill> getBillMapper() {
        return billMapper;
    }

    /**
     * Car model result set mapper
     *
     * @return car model result set mapper
     */
    public Mapper<CarModel> getCarModelMapper() {
        return carModelMapper;
    }

    /**
     * Car result set mapper
     *
     * @return car result set mapper
     */
    public Mapper<Car> getCarMapper() {
        return carMapper;
    }

    /**
     * Location result set mapper
     *
     * @return location result set mapper
     */
    public Mapper<Location> getLocationMapper() {
        return locationMapper;
    }

    /**
     * Reservation result set mapper
     *
     * @return reservation result set mapper
     */
    public Mapper<Reservation> getReservationMapper() {
        return reservationMapper;
    }

    /**
     * User result set mapper
     *
     * @return user result set mapper
     */
    public Mapper<User> getUserMapper() {
        return userMapper;
    }
}
