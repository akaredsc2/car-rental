package org.vitaly.dao.implementation;

import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.location.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-03-27.
 */
public class MysqlCarDao implements CarDao {
    private PooledConnection connection;

    MysqlCarDao(PooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Car> findById(Long id) {
        Car foundCar = null;

        try {
            PreparedStatement statement = connection.prepareStatement("select * from car where car_id = ?");
            statement.setLong(1, id);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                CarState state = CarStateEnum
                        .valueOf(resultSet.getString("car.car_status").toUpperCase())
                        .getState();

                LocationDao locationDao = DaoFactory.getMysqlDaoFactory().createLocationDao(connection);
                Location location = locationDao.findById(resultSet.getLong("car.location_id"))

                        // TODO: 2017-03-27 log
                        .orElseThrow(IllegalStateException::new);

                foundCar = new Car.Builder()
                        .setId(resultSet.getLong("car.car_id"))
                        .setState(state)
                        .setModel(resultSet.getString("car.model"))
                        .setPhotoUrl(resultSet.getString("car.photo_url"))
                        .setColor(resultSet.getString("car.color"))
                        .setPricePerDay(resultSet.getBigDecimal("car.price_per_day"))
                        .setLocation(location)
                        .build();
            }
        } catch (SQLException e) {

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return Optional.ofNullable(foundCar);
    }

    @Override
    public Optional<Long> findIdOfEntity(Car entity) {
        return null;
    }

    @Override
    public List<Car> getAll() {
        return null;
    }

    @Override
    public Optional<Long> create(Car car) {
        connection.initializeTransaction();

        Long createdId = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into car(car_status, model, photo_url, color, price_per_day, location_id) " +
                            "values (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, car.getState().toString());
            statement.setString(2, car.getModel());
            statement.setString(3, car.getPhotoUrl());
            statement.setString(4, car.getColor());
            statement.setBigDecimal(5, car.getPricePerDay());
            statement.setLong(6, car.getLocation().getId());

            statement.executeUpdate();

            connection.commit();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                createdId = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            connection.rollback();

            // TODO: 2017-03-27 log
            e.printStackTrace();
        }

        return Optional.ofNullable(createdId);
    }

    @Override
    public void update(Long id, Location entity) {

    }

    @Override
    public List<Car> getCarsAtLocation(Location location) {
        return null;
    }
}
