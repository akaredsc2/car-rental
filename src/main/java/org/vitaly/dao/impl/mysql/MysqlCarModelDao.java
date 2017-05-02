package org.vitaly.dao.impl.mysql;

import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.impl.mysql.factory.ResultSetMapperFactory;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.carModel.CarModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-04-22.
 */
public class MysqlCarModelDao implements CarModelDao {
    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
                    "FROM model " +
                    "WHERE model_id = ?";
    private static final String FIND_ID_OF_ENTITY_QUERY =
            "SELECT model_id " +
                    "FROM model " +
                    "WHERE model_name = ?";
    private static final String GET_ALL_QUERY =
            "SELECT * " +
                    "FROM model";
    private static final String CREATE_QUERY =
            "INSERT INTO model(model_name, doors, seats, horse_powers) " +
                    "VALUES(?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE model " +
                    "SET model_name = ?, photo_url = ?, doors = ?, seats = ?, horse_powers = ? " +
                    "WHERE model_id = ?";
    private static final String FIND_CAR_MODELS_WITH_PHOTOS_QUERY =
            "SELECT * " +
                    "FROM model " +
                    "WHERE photo_url IS NOT NULL";
    private static final String FIND_MODEL_OF_CAR_QUERY =
            "SELECT model.model_id, model.model_name, model.photo_url, model.doors, model.seats, model.horse_powers " +
                    "FROM model INNER JOIN car ON model.model_id = car.model_id " +
                    "WHERE car.car_id = ?";

    private static final String CAR_MODEL_MUST_NOT_BE_NULL = "Car model must not be null!";

    @Override
    public Optional<CarModel> findById(long id) {
        Mapper<CarModel> mapper = ResultSetMapperFactory.getInstance().getCarModelMapper();
        CarModel foundCarModel = DaoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, Collections.singletonMap(1, id));
        return Optional.ofNullable(foundCarModel);
    }

    @Override
    public Optional<Long> findIdOfEntity(CarModel carModel) {
        requireNotNull(carModel, CAR_MODEL_MUST_NOT_BE_NULL);

        Long foundModelId = DaoTemplate.executeSelectOne(FIND_ID_OF_ENTITY_QUERY,
                resultSet -> resultSet.getLong(1), Collections.singletonMap(1, carModel.getName()));
        return Optional.ofNullable(foundModelId);
    }

    @Override
    public List<CarModel> getAll() {
        Mapper<CarModel> mapper = ResultSetMapperFactory.getInstance().getCarModelMapper();
        return DaoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(CarModel carModel) {
        requireNotNull(carModel, CAR_MODEL_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, carModel.getName());
        parameterMap.put(2, carModel.getDoorCount());
        parameterMap.put(3, carModel.getSeatCount());
        parameterMap.put(4, carModel.getHorsePowerCount());

        Long createdId = DaoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, CarModel carModel) {
        requireNotNull(carModel, CAR_MODEL_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, carModel.getName());
        parameterMap.put(2, carModel.getPhotoUrl());
        parameterMap.put(3, carModel.getDoorCount());
        parameterMap.put(4, carModel.getSeatCount());
        parameterMap.put(5, carModel.getHorsePowerCount());
        parameterMap.put(6, id);

        return DaoTemplate.executeUpdate(UPDATE_QUERY, parameterMap);
    }

    @Override
    public List<CarModel> findCarModelsWithPhotos() {
        Mapper<CarModel> mapper = ResultSetMapperFactory.getInstance().getCarModelMapper();
        return DaoTemplate.executeSelect(FIND_CAR_MODELS_WITH_PHOTOS_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<CarModel> findModelOfCar(long carId) {
        Mapper<CarModel> mapper = ResultSetMapperFactory.getInstance().getCarModelMapper();
        CarModel foundModel = DaoTemplate
                .executeSelectOne(FIND_MODEL_OF_CAR_QUERY, mapper, Collections.singletonMap(1, carId));
        return Optional.ofNullable(foundModel);
    }
}
