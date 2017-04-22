package org.vitaly.dao.impl.mysql;

import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.dao.impl.mysql.mapper.CarModelMapper;
import org.vitaly.dao.impl.mysql.mapper.Mapper;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.util.InputChecker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    private Mapper<CarModel> mapper;
    private DaoTemplate daoTemplate;

    public MysqlCarModelDao(PooledConnection connection) {
        this(new CarModelMapper(), new DaoTemplate(connection));
    }

    public MysqlCarModelDao(Mapper<CarModel> mapper, DaoTemplate daoTemplate) {
        this.mapper = mapper;
        this.daoTemplate = daoTemplate;
    }

    @Override
    public Optional<CarModel> findById(long id) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, id);

        CarModel foundCarModel = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);
        return Optional.ofNullable(foundCarModel);
    }

    @Override
    public Optional<Long> findIdOfEntity(CarModel carModel) {
        InputChecker.requireNotNull(carModel, "Car model must not be null!");

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, carModel.getName());

        Long foundModelId = daoTemplate.executeSelectOne(FIND_ID_OF_ENTITY_QUERY,
                resultSet -> resultSet.getLong(1), parameterMap);
        return Optional.ofNullable(foundModelId);
    }

    @Override
    public List<CarModel> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(CarModel carModel) {
        InputChecker.requireNotNull(carModel, "Car model must not be null!");

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, carModel.getName());
        parameterMap.put(2, carModel.getDoorCount());
        parameterMap.put(3, carModel.getSeatCount());
        parameterMap.put(4, carModel.getHorsePowerCount());

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, CarModel carModel) {
        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, carModel.getName());
        parameterMap.put(2, carModel.getPhotoUrl());
        parameterMap.put(3, carModel.getDoorCount());
        parameterMap.put(4, carModel.getSeatCount());
        parameterMap.put(5, carModel.getHorsePowerCount());
        parameterMap.put(6, id);

        return daoTemplate.executeUpdate(UPDATE_QUERY, parameterMap);
    }

    @Override
    public List<CarModel> findCarModelsWithPhotos() {
        return daoTemplate.executeSelect(FIND_CAR_MODELS_WITH_PHOTOS_QUERY, mapper, Collections.emptyMap());
    }
}
