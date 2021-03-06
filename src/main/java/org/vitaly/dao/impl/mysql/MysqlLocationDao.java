package org.vitaly.dao.impl.mysql;

import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.impl.mysql.factory.ResultSetMapperFactory;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.location.Location;

import java.util.*;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * MySQL implementation of location dao
 */
public class MysqlLocationDao implements LocationDao {
    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM location " +
                    "WHERE location_id = ?";
    private static final String GET_ALL_QUERY =
            "SELECT * " +
                    "FROM location";
    private static final String CREATE_LOCATION_QUERY =
            "INSERT INTO location (state, city, street, building, photo_url) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_LOCATION_BY_CAR_ID_QUERY =
            "SELECT * " +
                    "FROM location " +
                    "WHERE location_id IN (SELECT location_id FROM car WHERE car_id = ?)";
    private static final String UPDATE_QUERY =
            "UPDATE location " +
                    "SET photo_url = ? " +
                    "WHERE location_id = ?";

    private static final String LOCATION_MUST_NOT_BE_NULL = "Location must not be null!";

    /**
     * @inheritDoc
     */
    @Override
    public Optional<Location> findById(long id) {
        return findLocationUsingQuery(id, FIND_BY_ID_QUERY);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Location> getAll() {
        Mapper<Location> mapper = ResultSetMapperFactory.getInstance().getLocationMapper();
        return DaoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Optional<Long> create(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, location.getState());
        parameterMap.put(2, location.getCity());
        parameterMap.put(3, location.getStreet());
        parameterMap.put(4, location.getBuilding());
        parameterMap.put(5, location.getPhotoUrl());

        Long createdId = DaoTemplate.executeInsert(CREATE_LOCATION_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int update(long id, Location updatedLocation) {
        requireNotNull(updatedLocation, LOCATION_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, updatedLocation.getPhotoUrl());
        parameterMap.put(2, id);

        return DaoTemplate.executeUpdate(UPDATE_QUERY, parameterMap);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Optional<Location> findLocationByCarId(long carId) {
        return findLocationUsingQuery(carId, FIND_LOCATION_BY_CAR_ID_QUERY);
    }

    /**
     * @inheritDoc
     */
    private Optional<Location> findLocationUsingQuery(long carId, String findLocationByCarIdQuery) {
        Mapper<Location> mapper = ResultSetMapperFactory.getInstance().getLocationMapper();
        Location foundLocation = DaoTemplate
                .executeSelectOne(findLocationByCarIdQuery, mapper, Collections.singletonMap(1, carId));
        return Optional.ofNullable(foundLocation);
    }
}
