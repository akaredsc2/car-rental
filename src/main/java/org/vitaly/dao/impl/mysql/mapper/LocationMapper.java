package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.model.location.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-07.
 */
public class LocationMapper implements Mapper<Location> {

    @Override
    public Location map(ResultSet resultSet) throws SQLException {
        return new Location.Builder()
                .setId(resultSet.getLong(LOCATION_LOCATION_ID))
                .setState(resultSet.getString(LOCATION_STATE))
                .setCity(resultSet.getString(LOCATION_CITY))
                .setStreet(resultSet.getString(LOCATION_STREET))
                .setBuilding(resultSet.getString(LOCATION_BUILDING))
                .setPhotoUrl(resultSet.getString(LOCATION_PHOTO_URL))
                .build();
    }
}
