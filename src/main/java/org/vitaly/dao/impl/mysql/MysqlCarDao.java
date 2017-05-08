package org.vitaly.dao.impl.mysql;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.impl.mysql.factory.ResultSetMapperFactory;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;

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
            "INSERT INTO car (model_id, registration_plate, color, price_per_day) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE car " +
                    "SET color = ?, price_per_day = ? " +
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
    private static final String CHANGE_CAR_STATE_QUERY =
            "UPDATE car " +
                    "SET car.car_status = ? " +
                    "WHERE car.car_id = ?";
    private static final String FIND_CAR_BY_RESERVATION_QUERY = "SELECT * " +
            "FROM car INNER JOIN reservation ON car.car_id = reservation.car_id " +
            "WHERE reservation_id = ?";

    private static final String CAR_MUST_NOT_BE_NULL = "Car must not be null!";
    private static final String FROM_NUMBER_MUST_NOT_BE_NULL = "From number must not be null!";
    private static final String TO_NUMBER_MUST_NOT_BE_NULL = "To number must not be null!";
    private static final String CAR_STATE_MUST_NOT_BE_NULL = "Car state must not be null!";

    @Override
    public Optional<Car> findById(long id) {
        Mapper<Car> mapper = ResultSetMapperFactory.getInstance().getCarMapper();
        Car car = DaoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, Collections.singletonMap(1, id));
        return Optional.ofNullable(car);
    }

    @Override
    public Optional<Long> findIdOfEntity(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Long foundId = DaoTemplate.executeSelectOne(FIND_ID_OF_CAR_QUERY,
                resultSet -> resultSet.getLong(1), Collections.singletonMap(1, car.getRegistrationPlate()));

        return Optional.ofNullable(foundId);
    }

    @Override
    public List<Car> getAll() {
        Mapper<Car> mapper = ResultSetMapperFactory.getInstance().getCarMapper();
        return DaoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, car.getCarModel().getId());
        parameterMap.put(2, car.getRegistrationPlate());
        parameterMap.put(3, car.getColor());
        parameterMap.put(4, car.getPricePerDay());

        Long createdId = DaoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, Car car) {
        requireNotNull(car, CAR_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, car.getColor());
        parameterMap.put(2, car.getPricePerDay());
        parameterMap.put(3, id);

        return DaoTemplate.executeUpdate(UPDATE_QUERY, parameterMap);
    }

    @Override
    public boolean moveCarToLocation(long carId, long locationId) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, locationId);
        parameterMap.put(2, carId);

        return DaoTemplate.executeUpdate(ADD_CAR_TO_LOCATION_QUERY, parameterMap) > 0;
    }

    @Override
    public List<Car> findCarsAtLocation(long locationId) {
        Mapper<Car> mapper = ResultSetMapperFactory.getInstance().getCarMapper();
        return DaoTemplate.executeSelect(GET_CARS_AT_LOCATION_QUERY, mapper, Collections.singletonMap(1, locationId));
    }

    @Override
    public List<Car> findCarsByModel(long carModelId) {
        Mapper<Car> mapper = ResultSetMapperFactory.getInstance().getCarMapper();
        return DaoTemplate.executeSelect(FIND_CAR_BY_MODEL_QUERY, mapper, Collections.singletonMap(1, carModelId));
    }

    @Override
    public List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to) {
        requireNotNull(from, FROM_NUMBER_MUST_NOT_BE_NULL);
        requireNotNull(to, TO_NUMBER_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, from);
        parameterMap.put(2, to);

        Mapper<Car> mapper = ResultSetMapperFactory.getInstance().getCarMapper();
        return DaoTemplate.executeSelect(FIND_CARS_WITH_PRICE_BETWEEN_QUERY, mapper, parameterMap);
    }

    @Override
    public boolean changeCarState(long carId, CarState state) {
        requireNotNull(state, CAR_STATE_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, state.toString());
        parameterMap.put(2, carId);

        return DaoTemplate.executeUpdate(CHANGE_CAR_STATE_QUERY, parameterMap) > 0;
    }

    @Override
    public Optional<Car> findCarByReservation(long reservationId) {
        Mapper<Car> mapper = ResultSetMapperFactory.getInstance().getCarMapper();

        Car car = DaoTemplate
                .executeSelectOne(FIND_CAR_BY_RESERVATION_QUERY, mapper, Collections.singletonMap(1, reservationId));
        return Optional.ofNullable(car);
    }
}
