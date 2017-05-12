package org.vitaly.dao.impl.mysql.mapper;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.carModel.CarModel;

import java.math.BigDecimal;
import java.sql.ResultSet;

import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-15.
 */
public class CarMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<Car> mapper = new CarMapper();

    @Test
    public void mapCorrectlySetsCarParameters() throws Exception {
        CarModel model = new CarModel.Builder().setId(10).build();
        Car expectedCar = new Car.Builder()
                .setId(3L)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setCarModel(model)
                .setRegistrationPlate("888")
                .setColor("blue")
                .setPricePerDay(BigDecimal.valueOf(140.0))
                .build();

        when(resultSet.getLong(CAR_CAR_ID)).thenReturn(expectedCar.getId());
        when(resultSet.getString(CAR_CAR_STATUS)).thenReturn(expectedCar.getState().toString());
        when(resultSet.getLong(CAR_MODEL_ID)).thenReturn(expectedCar.getCarModel().getId());
        when(resultSet.getString(CAR_REGISTRATION_PLATE)).thenReturn(expectedCar.getRegistrationPlate());
        when(resultSet.getString(CAR_COLOR)).thenReturn(expectedCar.getColor());
        when(resultSet.getBigDecimal(CAR_PRICE_PER_DAY)).thenReturn(expectedCar.getPricePerDay());

        Car actualCar = mapper.map(resultSet);

        Assert.assertThat(actualCar, allOf(
                Matchers.equalTo(expectedCar),
                hasId(expectedCar.getId())));
    }
}