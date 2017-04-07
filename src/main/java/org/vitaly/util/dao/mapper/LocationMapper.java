package org.vitaly.util.dao.mapper;

import org.vitaly.model.location.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by vitaly on 2017-04-07.
 */
public class LocationMapper implements Mapper<Location> {
    private static final String LOCATION_LOCATION_ID = "location.location_id";
    private static final String LOCATION_STATE = "location.state";
    private static final String LOCATION_CITY = "location.city";
    private static final String LOCATION_STREET = "location.street";
    private static final String LOCATION_BUILDING = "location.building";

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
