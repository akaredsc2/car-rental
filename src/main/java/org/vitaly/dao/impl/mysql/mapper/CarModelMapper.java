package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.model.carModel.CarModel;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelMapper implements Mapper<CarModel> {

    @Override
    public CarModel map(ResultSet resultSet) throws SQLException {
        return new CarModel.Builder()
                .setId(resultSet.getLong(MODEL_MODEL_ID))
                .setName(resultSet.getString(MODEL_MODEL_NAME))
                .setPhotoUrl(resultSet.getString(MODEL_PHOTO_URL))
                .setDoorCount(resultSet.getInt(MODEL_DOORS))
                .setSeatCount(resultSet.getInt(MODEL_SEATS))
                .setHorsePowerCount(resultSet.getInt(MODEL_HORSE_POWERS))
                .build();
    }
}
