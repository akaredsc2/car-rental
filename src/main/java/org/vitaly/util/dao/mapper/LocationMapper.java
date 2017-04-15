package org.vitaly.util.dao.mapper;

import org.vitaly.model.location.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.vitaly.util.TableAttributes.*;

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
                .setCars(new ArrayList<>())
                .build();
    }
}
