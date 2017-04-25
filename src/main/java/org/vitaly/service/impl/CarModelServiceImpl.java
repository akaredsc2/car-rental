package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelServiceImpl implements CarModelService {
    private TransactionFactory transactionFactory;
    private DtoMapperFactory dtoMapperFactory;

    CarModelServiceImpl(TransactionFactory transactionFactory) {
        this(transactionFactory, new DtoMapperFactory());
    }

    public CarModelServiceImpl(TransactionFactory transactionFactory, DtoMapperFactory dtoMapperFactory) {
        this.transactionFactory = transactionFactory;
        this.dtoMapperFactory = dtoMapperFactory;
    }

    @Override
    public boolean addCarModel(CarModelDto carModelDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModel carModel = dtoMapperFactory.getCarModelDtoMapper().mapDtoToEntity(carModelDto);
            CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
            boolean isCreated = carModelDao.create(carModel).isPresent();

            transaction.commit();

            return isCreated;
        }
    }

    @Override
    public List<CarModelDto> getAllMatchingCarModels(Predicate<CarModel> carModelPredicate) {
        DtoMapper<CarModel, CarModelDto> mapper = dtoMapperFactory.getCarModelDtoMapper();

        CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
        return carModelDao.getAll()
                .stream()
                .filter(carModelPredicate)
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCarModel(CarModelDto carModelDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModel carModel = dtoMapperFactory.getCarModelDtoMapper().mapDtoToEntity(carModelDto);
            CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
            carModelDao.update(carModelDto.getId(), carModel);

            transaction.commit();
        }
    }

    @Override
    public List<CarModelDto> findCarsWithPhotos() {
        DtoMapper<CarModel, CarModelDto> mapper = dtoMapperFactory.getCarModelDtoMapper();

        CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
        return carModelDao.findCarsWithPhotos()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
}

