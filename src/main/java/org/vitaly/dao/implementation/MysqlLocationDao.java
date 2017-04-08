package org.vitaly.dao.implementation;

import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.model.location.Location;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.LocationMapper;
import org.vitaly.util.dao.mapper.Mapper;

import java.util.*;

import static org.vitaly.util.ExceptionThrower.unsupported;
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
            "INSERT INTO location (state, city, street, building) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String LOCATION_COUNT_QUERY =
            "SELECT count(*) " +
                    "FROM location";

    private static final String LOCATION_MUST_NOT_BE_NULL = "Location must not be null!";

    private DaoTemplate daoTemplate;
    private Mapper<Location> mapper;

    MysqlLocationDao(PooledConnection connection) {
        this.mapper = new LocationMapper();
        this.daoTemplate = new DaoTemplate(connection);
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

        Long createdId = daoTemplate.executeInsert(CREATE_LOCATION_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Location entity) {
        unsupported();
        return 0;
    }

    @Override
    public int getLocationCount() {
        return daoTemplate
                .executeSelectOne(LOCATION_COUNT_QUERY, resultSet -> resultSet.getInt(1), Collections.emptyMap());
    }
}
