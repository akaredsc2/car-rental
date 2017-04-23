package org.vitaly.dao.impl.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.mapper.LocationMapper;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.location.Location;

import java.util.*;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-26.
 */
public class MysqlLocationDao implements LocationDao {
    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM location " +
                    "WHERE location_id = ?";
    private static final String FIND_ID_OF_LOCATION_QUERY =
            "SELECT location_id " +
                    "FROM location " +
                    "WHERE state = ? AND city = ? AND street = ? AND building = ?";
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
    private static final String CHANGE_IMAGE_URL_QUERY =
            "UPDATE location " +
                    "SET photo_url = ? " +
                    "WHERE location_id = ?";

    private static final String LOCATION_MUST_NOT_BE_NULL = "Location must not be null!";

    private static Logger logger = LogManager.getLogger(MysqlLocationDao.class.getName());

    private Mapper<Location> mapper;
    private DaoTemplate daoTemplate;

    public MysqlLocationDao(PooledConnection connection) {
        this(new LocationMapper(), new DaoTemplate(connection));
    }

    public MysqlLocationDao(Mapper<Location> mapper, DaoTemplate daoTemplate) {
        this.mapper = mapper;
        this.daoTemplate = daoTemplate;
    }

    @Override
    public Optional<Location> findById(long id) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, id);

        Location location = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(location);
    }

    @Override
    public Optional<Long> findIdOfEntity(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, location.getState());
        parameterMap.put(2, location.getCity());
        parameterMap.put(3, location.getStreet());
        parameterMap.put(4, location.getBuilding());

        Long foundId = daoTemplate
                .executeSelectOne(FIND_ID_OF_LOCATION_QUERY, resultSet -> resultSet.getLong(1), parameterMap);

        return Optional.ofNullable(foundId);
    }

    @Override
    public List<Location> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, location.getState());
        parameterMap.put(2, location.getCity());
        parameterMap.put(3, location.getStreet());
        parameterMap.put(4, location.getBuilding());
        parameterMap.put(5, location.getPhotoUrl());

        Long createdId = daoTemplate.executeInsert(CREATE_LOCATION_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Location entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error(e);
        throw e;
    }

    @Override
    public Optional<Location> findLocationByCarId(long carId) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, carId);

        Location foundLocation = daoTemplate.executeSelectOne(FIND_LOCATION_BY_CAR_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(foundLocation);
    }

    @Override
    public boolean changeImageUrl(long locationId, String imageUrl) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, imageUrl);
        parameterMap.put(2, locationId);

        return daoTemplate.executeUpdate(CHANGE_IMAGE_URL_QUERY, parameterMap) > 0;
    }
}
