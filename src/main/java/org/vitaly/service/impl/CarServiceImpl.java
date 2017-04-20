package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.car.Car;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class CarServiceImpl implements CarService {
    private TransactionFactory transactionFactory;

    public CarServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public List<Car> findCarsAtLocation(LocationDto locationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long locationId = locationDto.getId();
            CarDao carDao = transaction.getCarDao();
            return carDao.findCarsAtLocation(locationId);
        }
    }

    @Override
    public List<Car> findCarsByModel(String model) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarDao carDao = transaction.getCarDao();
            return carDao.findCarsByModel(model);
        }
    }

    @Override
    public List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarDao carDao = transaction.getCarDao();
            return carDao.findCarsWithPriceBetween(from, to);
        }
    }

    @Override
    public List<Car> getAllMatchingCars(Predicate<Car> carPredicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            CarDao carDao = transaction.getCarDao();
            return carDao.getAll()
                    .stream()
                    .filter(carPredicate)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Optional<Long> addNewCar(CarDto carDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Car car = new Car.Builder()
                    .setState(carDto.getState())
                    .setColor(carDto.getColor())
                    .setPricePerDay(carDto.getPricePerDay())
                    .setPhotoUrl(carDto.getPhotoUrl())
                    .setModel(carDto.getModel())
                    .setRegistrationPlate(carDto.getRegistrationPlate())
                    .build();

            CarDao carDao = transaction.getCarDao();
            Optional<Long> createdCarId = carDao.create(car);

            transaction.commit();

            return createdCarId;
        }
    }

    @Override
    public void updateCar(CarDto carDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Car car = new Car.Builder()
                    .setState(carDto.getState())
                    .setColor(carDto.getColor())
                    .setPricePerDay(carDto.getPricePerDay())
                    .setPhotoUrl(carDto.getPhotoUrl())
                    .setModel(carDto.getModel())
                    .setRegistrationPlate(carDto.getRegistrationPlate())
                    .build();

            CarDao carDao = transaction.getCarDao();
            carDao.update(carDto.getId(), car);

            transaction.commit();
        }
    }

    @Override
    public void moveCarToLocation(CarDto carDto, LocationDto locationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long carId = carDto.getId();
            long locationId = locationDto.getId();

            CarDao carDao = transaction.getCarDao();
            carDao.moveCarToLocation(carId, locationId);

            transaction.commit();
        }
    }
}
