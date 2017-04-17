package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.dao.abstraction.mapper.Mapper;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.vitaly.util.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-07.
 */
public class CarMapper implements Mapper<Car> {

    @Override
    public Car map(ResultSet resultSet) throws SQLException {
        CarState state = CarStateEnum
                .valueOf(resultSet.getString(CAR_CAR_STATUS).toUpperCase())
                .getState();

        return new Car.Builder()
                .setId(resultSet.getLong(CAR_CAR_ID))
                .setState(state)
                .setModel(resultSet.getString(CAR_MODEL))
                .setRegistrationPlate(resultSet.getString(CAR_REGISTRATION_PLATE))
                .setPhotoUrl(resultSet.getString(CAR_PHOTO_URL))
                .setColor(resultSet.getString(CAR_COLOR))
                .setPricePerDay(resultSet.getBigDecimal(CAR_PRICE_PER_DAY))
                .build();
    }
}
