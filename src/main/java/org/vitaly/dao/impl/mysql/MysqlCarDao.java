package org.vitaly.dao.impl.mysql;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.mapper.CarMapper;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.car.Car;

import java.math.BigDecimal;
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
            "INSERT INTO car (car_status, model_id, registration_plate, color, price_per_day) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE car " +
                    "SET car_status = ?, model_id = ?, registration_plate = ?, color = ?, price_per_day = ? " +
                    "WHERE car_id = ?";
    private static final String ADD_CAR_TO_LOCATION_QUERY =
            "UPDATE car " +
                    "SET location_id = ? " +
                    "WHERE car_id = ?";
    private static final String GET_CARS_AT_LOCATION_QUERY =
            "SELECT * " +
                    "FROM car " +
                    "WHERE location_id = ?";
    private static final String FIND_CAR_BY_MODEL_QUERY =
            "SELECT * " +
                    "FROM car " +
                    "WHERE model_id = ?";
    private static final String FIND_CARS_WITH_PRICE_BETWEEN_QUERY =
            "SELECT * " +
                    "FROM car " +
                    "WHERE price_per_day BETWEEN ? AND ?";

    private static final String CAR_MUST_NOT_BE_NULL = "Car must not be null!";

    private Mapper<Car> mapper;
    private DaoTemplate daoTemplate;

    public MysqlCarDao(PooledConnection connection) {
        this(new CarMapper(), new DaoTemplate(connection));
    }

    public MysqlCarDao(Mapper<Car> mapper, DaoTemplate daoTemplate) {
        this.mapper = mapper;
        this.daoTemplate = daoTemplate;
    }

    @Override
    public Optional<Car> findById(long id) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, id);

        Car car = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(car);
    }

    @Override
    public Optional<Long> findIdOfEntity(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, car.getRegistrationPlate());

        Long foundId = daoTemplate
                .executeSelectOne(FIND_ID_OF_CAR_QUERY, resultSet -> resultSet.getLong(1), parameterMap);

        return Optional.ofNullable(foundId);
    }

    @Override
    public List<Car> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        putCarParametersToMap(car, parameterMap);

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    private void putCarParametersToMap(Car car, Map<Integer, Object> parameterMap) {
        parameterMap.put(1, car.getState().toString());
        parameterMap.put(2, car.getCarModel().getId());
        parameterMap.put(3, car.getRegistrationPlate());
        parameterMap.put(4, car.getColor());
        parameterMap.put(5, car.getPricePerDay());
    }

    @Override
    public int update(long id, Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        putCarParametersToMap(car, parameterMap);
        parameterMap.put(7, id);

        return daoTemplate.executeUpdate(UPDATE_QUERY, parameterMap);
    }

    @Override
    public boolean moveCarToLocation(long carId, long locationId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, locationId);
        parameterMap.put(2, carId);

        return daoTemplate.executeUpdate(ADD_CAR_TO_LOCATION_QUERY, parameterMap) > 0;
    }

    @Override
    public List<Car> findCarsAtLocation(long locationId) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, locationId);

        return daoTemplate.executeSelect(GET_CARS_AT_LOCATION_QUERY, mapper, parameterMap);
    }

    @Override
    public List<Car> findCarsByModel(long carModelId) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, carModelId);

        return daoTemplate.executeSelect(FIND_CAR_BY_MODEL_QUERY, mapper, parameterMap);
    }

    @Override
    public List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to) {
        requireNotNull(from, "From number must not be null!");
        requireNotNull(to, "To number must not be null!");

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, from);
        parameterMap.put(2, to);

        return daoTemplate.executeSelect(FIND_CARS_WITH_PRICE_BETWEEN_QUERY, mapper, parameterMap);
    }

//    @Override
//    public List<String> findAllCarModels() {
//        return daoTemplate.executeSelect(FIND_ALL_CAR_MODELS_QUERY,
//                resultSet -> resultSet.getString(1), Collections.emptyMap());
//    }
}
