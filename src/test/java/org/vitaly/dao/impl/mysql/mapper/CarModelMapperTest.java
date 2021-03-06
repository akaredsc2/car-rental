package org.vitaly.dao.impl.mysql.mapper;

import org.junit.Test;
import org.vitaly.model.carModel.CarModel;

import java.sql.ResultSet;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<CarModel> mapper = new CarModelMapper();

    @Test
    public void mapCorrectlySetsNotificationParameters() throws Exception {
        CarModel expectedCarModel = new CarModel.Builder()
                .setId(10)
                .setName("Ford Focus")
                .setPhotoUrl("none")
                .setSeatCount(5)
                .setDoorCount(4)
                .setHorsePowerCount(150)
                .build();

        when(resultSet.getLong(MODEL_MODEL_ID)).thenReturn(expectedCarModel.getId());
        when(resultSet.getString(MODEL_MODEL_NAME)).thenReturn(expectedCarModel.getName());
        when(resultSet.getString(MODEL_PHOTO_URL)).thenReturn(expectedCarModel.getPhotoUrl());
        when(resultSet.getInt(MODEL_DOORS)).thenReturn(expectedCarModel.getDoorCount());
        when(resultSet.getInt(MODEL_SEATS)).thenReturn(expectedCarModel.getSeatCount());
        when(resultSet.getInt(MODEL_HORSE_POWERS)).thenReturn(expectedCarModel.getHorsePowerCount());

        CarModel actualCarModel = mapper.map(resultSet);

        assertThat(actualCarModel, allOf(
                equalTo(expectedCarModel),
                hasId(expectedCarModel.getId())));
    }
}