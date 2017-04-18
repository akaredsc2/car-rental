package org.vitaly.dao.impl.mysql.mapper;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.vitaly.data.TestData;
import org.vitaly.model.car.Car;

import java.sql.ResultSet;

import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-15.
 */
public class CarMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<Car> mapper = new CarMapper();

    @Test
    public void mapCorrectlySetsCarParameters() throws Exception {
        Car expectedCar = TestData.getInstance().getCar("car1");

        when(resultSet.getLong(CAR_CAR_ID)).thenReturn(expectedCar.getId());
        when(resultSet.getString(CAR_CAR_STATUS)).thenReturn(expectedCar.getState().toString());
        when(resultSet.getString(CAR_MODEL)).thenReturn(expectedCar.getModel());
        when(resultSet.getString(CAR_REGISTRATION_PLATE)).thenReturn(expectedCar.getRegistrationPlate());
        when(resultSet.getString(CAR_PHOTO_URL)).thenReturn(expectedCar.getPhotoUrl());
        when(resultSet.getString(CAR_COLOR)).thenReturn(expectedCar.getColor());
        when(resultSet.getBigDecimal(CAR_PRICE_PER_DAY)).thenReturn(expectedCar.getPricePerDay());

        Car actualCar = mapper.map(resultSet);

        Assert.assertThat(actualCar, allOf(
                Matchers.equalTo(expectedCar),
                hasId(expectedCar.getId())));
    }
}