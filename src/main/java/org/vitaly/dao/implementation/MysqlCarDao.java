package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.model.car.Car;
import org.vitaly.model.location.Location;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.CarMapper;
import org.vitaly.util.dao.mapper.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-27.
 */
public class MysqlCarDao implements CarDao {
    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM car " +
                    "WHERE car_id = ?";
    private static final String FIND_ID_OF_CAR_QUERY =
            "SELECT car_id " +
                    "FROM car " +
                    "WHERE registration_plate = ?";
    private static final String GET_ALL_QUERY =
            "SELECT * " +
                    "FROM car";
    private static final String CREATE_QUERY =
            "INSERT INTO car (car_status, model, registration_plate, photo_url, color, price_per_day) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE car " +
                    "SET car_status = ?, model = ?, registration_plate = ?, photo_url = ?, color = ?, price_per_day = ? " +
                    "WHERE car_id = ?";
    private static final String ADD_CAR_TO_LOCATION_QUERY =
            "UPDATE car " +
                    "SET location_id = ? " +
                    "WHERE car_id = ?";
    private static final String GET_CARS_AT_LOCATION_QUERY =
            "SELECT * " +
                    "FROM car " +
                    "WHERE location_id = ?";

    private static final String CAR_MUST_NOT_BE_NULL = "Car must not be null!";
    private static final String LOCATION_MUST_NOT_BE_NULL = "Location must not be null!";

    private static final Logger logger = LogManager.getLogger(MysqlCarDao.class.getName());

    private PooledConnection connection;
    private Mapper<Car> mapper;
    private DaoTemplate daoTemplate;

    MysqlCarDao(PooledConnection connection) {
        this.connection = connection;
        this.mapper = new CarMapper();
        this.daoTemplate = new DaoTemplate(connection);
    }

    @Override
    public Optional<Car> findById(long id) {
        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, id);

        Car car = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(car);
    }

    @Override
    public OptionalLong findIdOfEntity(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, car.getRegistrationPlate());

        Long foundId = daoTemplate
                .executeSelectOne(FIND_ID_OF_CAR_QUERY, resultSet -> resultSet.getLong(1), parameterMap);

        return foundId == null ? OptionalLong.empty() : OptionalLong.of(foundId);
    }

    @Override
    public List<Car> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, new TreeMap<>());
    }

    @Override
    public OptionalLong create(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        connection.initializeTransaction();

        OptionalLong createdId = OptionalLong.empty();
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            setCarParametersInStatement(car, statement);

            statement.executeUpdate();

            connection.commit();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                createdId = OptionalLong.of(resultSet.getLong(1));
            }

            resultSet.close();
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error while creating car. Rolling back transaction.", e);
        }

        return createdId;
    }

    private void setCarParametersInStatement(Car car, PreparedStatement statement) throws SQLException {
        statement.setString(1, car.getState().toString());
        statement.setString(2, car.getModel());
        statement.setString(3, car.getRegistrationPlate());
        statement.setString(4, car.getPhotoUrl());
        statement.setString(5, car.getColor());
        statement.setBigDecimal(6, car.getPricePerDay());
    }

    @Override
    public int update(long id, Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        connection.initializeTransaction();

        int updateCount = 0;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            setCarParametersInStatement(car, statement);
            statement.setLong(7, id);

            updateCount = statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error while updating car. Rolling back transaction.", e);
        }

        return updateCount;
    }

    @Override
    public boolean addCarToLocation(Car car, Location location) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        boolean updateResult = false;

        connection.initializeTransaction();

        try (PreparedStatement statement = connection.prepareStatement(ADD_CAR_TO_LOCATION_QUERY)) {
            statement.setLong(1, location.getId());
            statement.setLong(2, car.getId());

            int updatedRecordCount = statement.executeUpdate();
            updateResult = updatedRecordCount > 0;
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.error("Error while adding car to location. Rolling back transaction.", e);
        }

        return updateResult;
    }

    @Override
    public List<Car> getCarsAtLocation(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, location.getId());

        return daoTemplate.executeSelect(GET_CARS_AT_LOCATION_QUERY, mapper, parameterMap);
    }
}
