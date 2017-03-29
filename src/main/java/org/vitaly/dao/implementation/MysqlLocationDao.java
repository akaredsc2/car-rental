package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.model.location.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-26.
 */
public class MysqlLocationDao implements LocationDao {
    private static final String LOCATION_LOCATION_ID = "location.location_id";
    private static final String LOCATION_STATE = "location.state";
    private static final String LOCATION_CITY = "location.city";
    private static final String LOCATION_STREET = "location.street";
    private static final String LOCATION_BUILDING = "location.building";

    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
            "FROM location " +
            "WHERE location_id = ?";
    private static final String FIND_ID_OF_LOCATION_QUERY =
            "SELECT location_id " +
            "FROM location " +
            "WHERE state = ? AND city = ? AND street = ? AND building = ?";
    private static final String ALL_LOCATIONS_QUERY =
            "SELECT * " +
            "FROM location";
    private static final String CREATE_LOCATION_QUERY =
            "INSERT INTO location (state, city, street, building) " +
            "VALUES (?, ?, ?, ?)";
    private static final String LOCATION_COUNT_QUERY =
            "SELECT count(*) " +
            "FROM location";

    private static final String ID_MUST_NOT_BE_NULL = "Id must not be null!";
    private static final String LOCATION_MUST_NOT_BE_NULL = "Location must not be null!";

    private static final Logger logger = LogManager.getLogger(MysqlLocationDao.class.getName());

    private PooledConnection connection;

    MysqlLocationDao(PooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Location> findById(long id) {
        requireNotNull(id, ID_MUST_NOT_BE_NULL);

        Location location = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                location = buildLocationFromResultSetEntry(resultSet);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while finding location by id.", e);
        }

        return Optional.ofNullable(location);
    }

    private Location buildLocationFromResultSetEntry(ResultSet resultSet) throws SQLException {
        return new Location.Builder()
                .setId(resultSet.getLong(LOCATION_LOCATION_ID))
                .setState(resultSet.getString(LOCATION_STATE))
                .setCity(resultSet.getString(LOCATION_CITY))
                .setStreet(resultSet.getString(LOCATION_STREET))
                .setBuilding(resultSet.getString(LOCATION_BUILDING))
                .setCars(new ArrayList<>())
                .build();
    }

    @Override
    public OptionalLong findIdOfEntity(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        OptionalLong foundId = OptionalLong.empty();

        try (PreparedStatement statement = connection.prepareStatement(FIND_ID_OF_LOCATION_QUERY)) {
            statement.setString(1, location.getState());
            statement.setString(2, location.getCity());
            statement.setString(3, location.getStreet());
            statement.setString(4, location.getBuilding());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                foundId = OptionalLong.of(resultSet.getLong(LOCATION_LOCATION_ID));
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while finding id of location.", e);
        }

        return foundId;
    }

    @Override
    public List<Location> getAll() {
        List<Location> locationList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_LOCATIONS_QUERY)) {
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Location location = buildLocationFromResultSetEntry(resultSet);
                locationList.add(location);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while getting all locations.", e);
        }

        return locationList;
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
        try (PreparedStatement statement = connection.prepareStatement(LOCATION_COUNT_QUERY)) {
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while getting location count.", e);
        }

        return 0;
    }
}
