package org.vitaly.dao.implementation;

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

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-27.
 */
public class MysqlCarDao implements CarDao {
    private static final String ID_MUST_NOT_BE_NULL = "Id must not be null!";
    private static final String FIND_BY_ID_QUERY =
            "select * " +
            "from car " +
            "where car_id = ?";
    private static final String CAR_STATUS = "car.car_status";
    private static final String CAR_ID = "car.car_id";
    private static final String CAR_MODEL = "car.model";
    private static final String CAR_REGISTRATION_PLATE = "car.registration_plate";
    private static final String CAR_PHOTO_URL = "car.photo_url";
    private static final String CAR_COLOR = "car.color";
    private static final String CAR_PRICE_PER_DAY = "car.price_per_day";
    private static final String CAR_MUST_NOT_BE_NULL = "Car must not be null!";
    private static final String FIND_ID_OF_CAR_QUERY = "select car_id " +
            "from car " +
            "where registration_plate = ?";
    private static final String GET_ALL_QUERY =
            "select * " +
            "from car";
    private static final String CAR_MUST_NOT_BE_NULL1 = "Car must not be null!";
    private static final String CREATE_QUERY =
            "insert into car(car_status, model, registration_plate, photo_url, color, price_per_day) " +
            "values (?, ?, ?, ?, ?, ?)";
    private static final String ID_MUST_NOT_BE_NULL1 = "Id must not be null!";
    private static final String CAR_MUST_NOT_BE_NULL2 = "Car must not be null!";
    private static final String UPDATE_QUERY =
            "update car " +
            "set car_status = ?, model = ?, registration_plate = ?, photo_url = ?, color = ?, price_per_day = ? " +
            "where car_id = ?";
    private static final String CAR_MUST_NOT_BE_NULL3 = "Car must not be null!";
    private static final String LOCATION_MUST_NOT_BE_NULL = "Location must not be null!";
    private static final String ADD_CAR_TO_LOCATION_QUERY =
            "update car " +
            "set location_id = ? " +
            "where car_id = ?";
    private static final String LOCATION_MUST_NOT_BE_NULL1 = "Location must not be null!";
    private static final String GET_CARS_AT_LOCATION_QUERY =
            "select car_id, car_status, model, registration_plate, photo_url, color, price_per_day " +
            "from car inner join location loc on car.location_id = loc.location_id " +
            "where loc.location_id = ?";
    private PooledConnection connection;

    MysqlCarDao(PooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Car> findById(Long id) {
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

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return Optional.ofNullable(foundCar);
    }

    private Car buildCarFromResultSetEntry(ResultSet resultSet) throws SQLException {
        CarState state = CarStateEnum
                .valueOf(resultSet.getString(CAR_STATUS).toUpperCase())
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
    public Optional<Long> findIdOfEntity(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Long foundId = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_ID_OF_CAR_QUERY)) {
            statement.setString(1, car.getRegistrationPlate());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                foundId = resultSet.getLong(1);
            }

            resultSet.close();
        } catch (SQLException e) {

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return Optional.ofNullable(foundId);
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

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return cars;
    }

    @Override
    public Optional<Long> create(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL1);

        connection.initializeTransaction();

        Long createdId = null;
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            setCarParametersInStatement(car, statement);

            statement.executeUpdate();

            connection.commit();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                createdId = resultSet.getLong(1);
            }

            resultSet.close();
        } catch (SQLException e) {
            connection.rollback();

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return Optional.ofNullable(createdId);
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
    public int update(Long id, Car car) {
        requireNotNull(id, ID_MUST_NOT_BE_NULL1);
        requireNotNull(car, CAR_MUST_NOT_BE_NULL2);

        connection.initializeTransaction();

        int updateCount = 0;
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            setCarParametersInStatement(car, statement);
            statement.setLong(7, id);

            updateCount = statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return updateCount;
    }

    @Override
    public boolean addCarToLocation(Car car, Location location) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL3);
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

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return updateResult;
    }

    @Override
    public List<Car> getCarsAtLocation(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL1);

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

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return cars;
    }
}
