package org.vitaly.util.dao.mapper;

import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vitaly on 2017-04-07.
 */
public class CarMapper implements Mapper<Car> {
    private static final String CAR_CAR_STATUS = "car.car_status";
    private static final String CAR_ID = "car.car_id";
    private static final String CAR_MODEL = "car.model";
    private static final String CAR_REGISTRATION_PLATE = "car.registration_plate";
    private static final String CAR_PHOTO_URL = "car.photo_url";
    private static final String CAR_COLOR = "car.color";
    private static final String CAR_PRICE_PER_DAY = "car.price_per_day";

    @Override
    public Car map(ResultSet resultSet) throws SQLException {
        CarState state = CarStateEnum
                .valueOf(resultSet.getString(CAR_CAR_STATUS).toUpperCase())
                .getState();

        return new Car.Builder()
                .setId(resultSet.getLong(CAR_ID))
                .setState(state)
                .setModel(resultSet.getString(CAR_MODEL))
                .setRegistrationPlate(resultSet.getString(CAR_REGISTRATION_PLATE))
                .setPhotoUrl(resultSet.getString(CAR_PHOTO_URL))
                .setColor(resultSet.getString(CAR_COLOR))
                .setPricePerDay(resultSet.getBigDecimal(CAR_PRICE_PER_DAY))
                .build();
    }
}
