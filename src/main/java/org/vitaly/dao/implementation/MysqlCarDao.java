package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.location.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-27.
 */
public class MysqlCarDao implements CarDao {
    private static final String CAR_CAR_STATUS = "car.car_status";
    private static final String CAR_ID = "car.car_id";
    private static final String CAR_MODEL = "car.model";
    private static final String CAR_REGISTRATION_PLATE = "car.registration_plate";
    private static final String CAR_PHOTO_URL = "car.photo_url";
    private static final String CAR_COLOR = "car.color";
    private static final String CAR_PRICE_PER_DAY = "car.price_per_day";

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
            "SELECT" +
            "  car_id," +
            "  car_status," +
            "  model," +
            "  registration_plate," +
            "  photo_url," +
            "  color," +
            "  price_per_day " +
            "FROM car " +
            "  INNER JOIN location loc ON car.location_id = loc.location_id " +
            "WHERE loc.location_id = ?";

    private static final String ID_MUST_NOT_BE_NULL = "Id must not be null!";
    private static final String CAR_MUST_NOT_BE_NULL = "Car must not be null!";
    private static final String LOCATION_MUST_NOT_BE_NULL = "Location must not be null!";

    private static final Logger logger = LogManager.getLogger(MysqlCarDao.class.getName());

    private PooledConnection connection;

    MysqlCarDao(PooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Car> findById(long id) {
        requireNotNull(id, ID_MUST_NOT_BE_NULL);

        Car foundCar = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                foundCar = buildCarFromResultSetEntry(resultSet);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while finding car by id.", e);
        }

        return Optional.ofNullable(foundCar);
    }

    private Car buildCarFromResultSetEntry(ResultSet resultSet) throws SQLException {
        CarState state = CarStateEnum
                .valueOf(resultSet.getString(CAR_CAR_STATUS).toUpperCase())
                .getState();

        return new Car.Builder()
                .setId(resultSet.getLong(CAR_ID))
                .setState(state)
                .setModel(resultSet.getString(CAR_MODEL))
                .setRegistrationPlate(resultSet.getString(CAR_REGISTRATION_PLATE))
                .setPhotoUrl(resultSet.getString(CAR_PHOTO_URL))
                .setColor(resultSet.getString(CAR_COLOR))
                .setPricePerDay(resultSet.getBigDecimal(CAR_PRICE_PER_DAY))
                .build();
    }

    @Override
    public OptionalLong findIdOfEntity(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        OptionalLong foundId = OptionalLong.empty();

        try (PreparedStatement statement = connection.prepareStatement(FIND_ID_OF_CAR_QUERY)) {
            statement.setString(1, car.getRegistrationPlate());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                foundId = OptionalLong.of(resultSet.getLong(1));
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while finding id of car.", e);
        }

        return foundId;
    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY)) {
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Car nextCar = buildCarFromResultSetEntry(resultSet);
                cars.add(nextCar);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while getting all cars.", e);
        }

        return cars;
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

        List<Car> cars = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(GET_CARS_AT_LOCATION_QUERY)) {
            statement.setLong(1, location.getId());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Car nextCar = buildCarFromResultSetEntry(resultSet);
                cars.add(nextCar);
            }
        } catch (SQLException e) {
            logger.error("Error while getting cars at location.", e);
        }

        return cars;
    }
}
