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
    public Optional<Long> findIdOfEntity(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, car.getRegistrationPlate());

        Long foundId = daoTemplate
                .executeSelectOne(FIND_ID_OF_CAR_QUERY, resultSet -> resultSet.getLong(1), parameterMap);

        return Optional.ofNullable(foundId);
    }

    @Override
    public List<Car> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, new TreeMap<>());
    }

    @Override
    public Optional<Long> create(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        putCarParametersToMap(car, parameterMap);

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    private void putCarParametersToMap(Car car, Map<Integer, Object> parameterMap) {
        parameterMap.put(1, car.getState().toString());
        parameterMap.put(2, car.getModel());
        parameterMap.put(3, car.getRegistrationPlate());
        parameterMap.put(4, car.getPhotoUrl());
        parameterMap.put(5, car.getColor());
        parameterMap.put(6, car.getPricePerDay());
    }

    @Override
    public int update(long id, Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        putCarParametersToMap(car, parameterMap);
        parameterMap.put(7, id);

        return daoTemplate.executeUpdate(UPDATE_QUERY, parameterMap);
    }

    @Override
    public boolean addCarToLocation(Car car, Location location) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        TreeMap<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, location.getId());
        parameterMap.put(2, car.getId());

        int updatedRecordCount = daoTemplate.executeUpdate(ADD_CAR_TO_LOCATION_QUERY, parameterMap);
        return updatedRecordCount > 0;
    }

    @Override
    public List<Car> getCarsAtLocation(Location location) {
        requireNotNull(location, LOCATION_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, location.getId());

        return daoTemplate.executeSelect(GET_CARS_AT_LOCATION_QUERY, mapper, parameterMap);
    }
}
