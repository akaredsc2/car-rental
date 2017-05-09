package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.dao.exception.DaoException;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.carModel.CarModel;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-07.
 */
public class CarMapper implements Mapper<Car> {

    @Override
    public Car map(ResultSet resultSet) throws SQLException {
        CarState state = CarStateEnum
                .stateOf(resultSet.getString(CAR_CAR_STATUS).toUpperCase())
                .orElseThrow(() -> new DaoException("Wrong car state!"));

        long carModelId = resultSet.getLong(CAR_MODEL_ID);
        CarModel carModel = CarModel.createDummyCarModelWithId(carModelId);

        return new Car.Builder()
                .setId(resultSet.getLong(CAR_CAR_ID))
                .setState(state)
                .setCarModel(carModel)
                .setRegistrationPlate(resultSet.getString(CAR_REGISTRATION_PLATE))
                .setColor(resultSet.getString(CAR_COLOR))
                .setPricePerDay(resultSet.getBigDecimal(CAR_PRICE_PER_DAY))
                .build();
    }
}
