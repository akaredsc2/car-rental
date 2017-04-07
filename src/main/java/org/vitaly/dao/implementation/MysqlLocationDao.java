package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.model.location.Location;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.LocationMapper;
import org.vitaly.util.dao.mapper.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
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

    private static final Logger logger = LogManager.getLogger(MysqlLocationDao.class.getName());

    private PooledConnection connection;
    private DaoTemplate daoTemplate;
    private Mapper<Location> mapper;

    MysqlLocationDao(PooledConnection connection) {
        this.connection = connection;
        this.mapper = new LocationMapper();
        this.daoTemplate = new DaoTemplate(connection);
    }

    @Override
    public Optional<Location> findById(long id) {
        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, id);

        Location location = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(location);
    }

    @Override
    public OptionalLong findIdOfEntity(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, location.getState());
        parameterMap.put(2, location.getCity());
        parameterMap.put(3, location.getStreet());
        parameterMap.put(4, location.getBuilding());

        Long foundId = daoTemplate
                .executeSelectOne(FIND_ID_OF_LOCATION_QUERY, resultSet -> resultSet.getLong(1), parameterMap);

        return foundId == null ? OptionalLong.empty() : OptionalLong.of(foundId);
    }

    @Override
    public List<Location> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, new TreeMap<>());
    }

    @Override
    public OptionalLong create(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        connection.initializeTransaction();

        OptionalLong createdId = OptionalLong.empty();
        try (PreparedStatement statement = connection.prepareStatement(CREATE_LOCATION_QUERY, RETURN_GENERATED_KEYS)) {
            statement.setString(1, location.getState());
            statement.setString(2, location.getCity());
            statement.setString(3, location.getStreet());
            statement.setString(4, location.getBuilding());

            // TODO: 2017-03-26 transaction isolation
            statement.executeUpdate();

            connection.commit();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                createdId = OptionalLong.of(resultSet.getLong(1));
            }

            resultSet.close();
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error while creating location. Rolling back transaction.", e);
        }

        return createdId;
    }

    @Override
    public int update(long id, Location entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error("Error while calling unsupported operation.", e);
        throw e;
    }

    @Override
    public int getLocationCount() {
        return daoTemplate.executeSelectOne(LOCATION_COUNT_QUERY, resultSet -> resultSet.getInt(1), new TreeMap<>());
    }
}
