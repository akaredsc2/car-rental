package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.CarModelDtoMapper;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelServiceImpl implements CarModelService {
    private TransactionFactory transactionFactory;

    public CarModelServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public boolean addCarModel(CarModelDto carModelDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModel carModel = new CarModelDtoMapper().mapDtoToEntity(carModelDto);
            CarModelDao carModelDao = transaction.getCarModelDao();
            boolean isCreated = carModelDao.create(carModel).isPresent();

            transaction.commit();

            return isCreated;
        }
    }

    @Override
    public List<CarModelDto> getAllMatchingCarModels(Predicate<CarModel> carModelPredicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModelDtoMapper mapper = new CarModelDtoMapper();
            CarModelDao carModelDao = transaction.getCarModelDao();
            return carModelDao.getAll()
                    .stream()
                    .filter(carModelPredicate)
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void updateCarModel(CarModelDto carModelDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModel carModel = new CarModelDtoMapper().mapDtoToEntity(carModelDto);
            CarModelDao carModelDao = transaction.getCarModelDao();
            carModelDao.update(carModelDto.getId(), carModel);

            transaction.commit();
        }
    }

    @Override
    public List<CarModelDto> findCarsWithPhotos() {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModelDtoMapper mapper = new CarModelDtoMapper();
            CarModelDao carModelDao = transaction.getCarModelDao();
            return carModelDao.findCarsWithPhotos()
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }
}
