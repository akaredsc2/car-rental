package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.Transaction;
import org.vitaly.model.car.Car;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.vitaly.model.car.CarStateEnum.*;

/**
 * Created by vitaly on 2017-04-10.
 */
public class CarServiceImpl implements CarService {

    @Override
    public boolean addNewCar(CarDto carDto) {
        try (Transaction transaction = Transaction.startTransaction()) {
            Car car = DtoMapperFactory.getInstance()
                    .getCarDtoMapper()
                    .mapDtoToEntity(carDto);
            CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
            boolean isCarCreated = carDao.create(car).isPresent();

            transaction.commit();

            return isCarCreated;
        }
    }

    @Override
    public List<CarDto> findCarsAtLocation(LocationDto locationDto) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        long locationId = locationDto.getId();
        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
        return carDao.findCarsAtLocation(locationId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> findCarsByModel(CarModelDto carModelDto) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        long carModelId = carModelDto.getId();
        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
        return carDao.findCarsByModel(carModelId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> findCarsWithPriceBetween(BigDecimal from, BigDecimal to) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
        return carDao.findCarsWithPriceBetween(from, to)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> getAllCars() {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
        return carDao.getAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateCar(CarDto carDto) {
        try (Transaction transaction = Transaction.startTransaction()) {
            CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
            boolean isAbleToBeUpdated = carDao.findById(carDto.getId())
                    .map(Car::getState)
                    .filter(state -> state == AVAILABLE.getState() || state == UNAVAILABLE.getState())
                    .isPresent();

            if (isAbleToBeUpdated) {
                Car car = DtoMapperFactory.getInstance()
                        .getCarDtoMapper()
                        .mapDtoToEntity(carDto);

                boolean isUpdated = carDao.update(carDto.getId(), car) > 0;

                transaction.commit();

                return isUpdated;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean moveCarToLocation(CarDto carDto, LocationDto locationDto) {
        try (Transaction transaction = Transaction.startTransaction()) {
            long carId = carDto.getId();
            CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();

            boolean isCarAbleToBeMoved = carDao.findById(carId)
                    .filter(car -> car.getState() == UNAVAILABLE.getState())
                    .isPresent();

            if (isCarAbleToBeMoved) {
                long locationId = locationDto.getId();

                carDao.moveCarToLocation(carId, locationId);

                transaction.commit();

                return true;
            }
            return false;
        }
    }

    @Override
    public boolean changeCarState(CarDto carDto, String carState) {
        try (Transaction transaction = Transaction.startTransaction()) {
            Car car = DtoMapperFactory.getInstance()
                    .getCarDtoMapper()
                    .mapDtoToEntity(carDto);

            boolean isStateChanged = changeState(car, carState);
            if (isStateChanged) {
                MysqlDaoFactory.getInstance()
                        .getCarDao()
                        .changeCarState(car.getId(), car.getState());

                transaction.commit();

                return true;
            } else {
                return false;
            }
        }
    }

    private boolean changeState(Car car, String carState) {
        if (carState.equalsIgnoreCase(UNAVAILABLE.getState().toString())) {
            return car.makeUnavailable();
        }
        if (carState.equalsIgnoreCase(AVAILABLE.getState().toString())) {
            return car.makeAvailable();
        }
        if (carState.equalsIgnoreCase(RESERVED.getState().toString())) {
            return car.reserve();
        }
        if (carState.equalsIgnoreCase(SERVED.getState().toString())) {
            return car.serve();
        }
        if (carState.equalsIgnoreCase(RETURNED.getState().toString())) {
            return car.doReturn();
        }
        return false;
    }
}
