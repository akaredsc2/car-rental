package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.car.Car;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class CarServiceImpl implements CarService {
    private TransactionFactory transactionFactory;
    private DtoMapperFactory dtoMapperFactory;

    CarServiceImpl(TransactionFactory transactionFactory) {
        this(transactionFactory, new DtoMapperFactory());
    }

    public CarServiceImpl(TransactionFactory transactionFactory, DtoMapperFactory dtoMapperFactory) {
        this.transactionFactory = transactionFactory;
        this.dtoMapperFactory = dtoMapperFactory;
    }

    @Override
    public boolean addNewCar(CarDto carDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Car car = dtoMapperFactory.getCarDtoMapper().mapDtoToEntity(carDto);
            CarDao carDao = transaction.getCarDao();
            boolean isCarCreated = carDao.create(car).isPresent();

            transaction.commit();

            return isCarCreated;
        }
    }

    @Override
    public List<CarDto> findCarsAtLocation(LocationDto locationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Car, CarDto> mapper = dtoMapperFactory.getCarDtoMapper();

            long locationId = locationDto.getId();
            CarDao carDao = transaction.getCarDao();
            return carDao.findCarsAtLocation(locationId)
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CarDto> findCarsByModel(CarModelDto carModelDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Car, CarDto> mapper = dtoMapperFactory.getCarDtoMapper();

            long carModelId = carModelDto.getId();
            CarDao carDao = transaction.getCarDao();
            return carDao.findCarsByModel(carModelId)
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CarDto> findCarsWithPriceBetween(BigDecimal from, BigDecimal to) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Car, CarDto> mapper = dtoMapperFactory.getCarDtoMapper();

            CarDao carDao = transaction.getCarDao();
            return carDao.findCarsWithPriceBetween(from, to)
                    .stream()
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<CarDto> getAllMatchingCars(Predicate<Car> carPredicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Car, CarDto> mapper = dtoMapperFactory.getCarDtoMapper();

            CarDao carDao = transaction.getCarDao();
            return carDao.getAll()
                    .stream()
                    .filter(carPredicate)
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void updateCar(CarDto carDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Car car = dtoMapperFactory.getCarDtoMapper().mapDtoToEntity(carDto);

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
