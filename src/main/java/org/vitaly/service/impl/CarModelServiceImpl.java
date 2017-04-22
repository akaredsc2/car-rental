package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarModelDto;

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
            CarModel carModel = new CarModel.Builder()
                    .setName(carModelDto.getName())
                    .setDoorCount(carModelDto.getDoorCount())
                    .setSeatCount(carModelDto.getSeatCount())
                    .setHorsePowerCount(carModelDto.getHorsePowerCount())
                    .build();

            CarModelDao carModelDao = transaction.getCarModelDao();
            boolean isCreated = carModelDao.create(carModel).isPresent();

            transaction.commit();

            return isCreated;
        }
    }

    @Override
    public List<CarModel> getAllMatchingCarModels(Predicate<CarModel> carModelPredicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModelDao carModelDao = transaction.getCarModelDao();
            return carModelDao.getAll()
                    .stream()
                    .filter(carModelPredicate)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void updateCarModel(CarModelDto carModelDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModel carModel = new CarModel.Builder()
                    .setName(carModelDto.getName())
                    .setPhotoUrl(carModelDto.getPhotoUrl())
                    .setDoorCount(carModelDto.getDoorCount())
                    .setSeatCount(carModelDto.getSeatCount())
                    .setHorsePowerCount(carModelDto.getHorsePowerCount())
                    .build();

            CarModelDao carModelDao = transaction.getCarModelDao();
            carModelDao.update(carModelDto.getId(), carModel);

            transaction.commit();
        }
    }

    @Override
    public List<CarModel> findCarsWithPhotos() {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarModelDao carModelDao = transaction.getCarModelDao();
            return carModelDao.findCarsWithPhotos();
        }
    }
}
