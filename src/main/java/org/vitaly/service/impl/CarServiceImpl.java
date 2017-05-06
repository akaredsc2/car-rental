package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.vitaly.model.car.CarStateEnum.AVAILABLE;
import static org.vitaly.model.car.CarStateEnum.UNAVAILABLE;

/**
 * Created by vitaly on 2017-04-10.
 */
public class CarServiceImpl implements CarService {

    @Override
    public boolean addNewCar(CarDto carDto) {
        TransactionManager.startTransaction();

        Car car = DtoMapperFactory.getInstance()
                .getCarDtoMapper()
                .mapDtoToEntity(carDto);

        boolean isCarCreated = MysqlDaoFactory.getInstance()
                .getCarDao()
                .create(car)
                .isPresent();

        TransactionManager.commit();

        return isCarCreated;
    }

    @Override
    public List<CarDto> findCarsAtLocation(LocationDto locationDto) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        long locationId = locationDto.getId();
        return MysqlDaoFactory.getInstance()
                .getCarDao()
                .findCarsAtLocation(locationId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> findCarsByModel(CarModelDto carModelDto) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        long carModelId = carModelDto.getId();
        return MysqlDaoFactory.getInstance()
                .getCarDao()
                .findCarsByModel(carModelId)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> findCarsWithPriceBetween(BigDecimal from, BigDecimal to) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getCarDao()
                .findCarsWithPriceBetween(from, to)
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> getAllCars() {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getCarDao()
                .getAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateCar(CarDto carDto) {
        TransactionManager.startTransaction();

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

            TransactionManager.commit();
            return isUpdated;
        } else {
            TransactionManager.rollback();
            return false;
        }
    }

    @Override
    public boolean moveCarToLocation(CarDto carDto, LocationDto locationDto) {
        TransactionManager.startTransaction();

        long carId = carDto.getId();
        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();

        boolean isCarAbleToBeMoved = carDao.findById(carId)
                .filter(car -> car.getState() == UNAVAILABLE.getState())
                .isPresent();

        if (isCarAbleToBeMoved) {
            long locationId = locationDto.getId();

            boolean isCarMoved = carDao.moveCarToLocation(carId, locationId);

            TransactionManager.commit();
            return isCarMoved;
        } else {
            TransactionManager.rollback();
            return false;
        }
    }

    @Override
    public boolean changeCarState(CarDto carDto, String carState) {
        TransactionManager.startTransaction();

        // TODO: 06.05.17 test
        // TODO: 06.05.17 refactor
        long carId = carDto.getId();
        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();

        boolean isCarPartOfActiveReservation = MysqlDaoFactory.getInstance()
                .getReservationDao()
                .isCarPartOfActiveReservations(carId);

        boolean isAbleToChangeState = carDao
                .findById(carId)
                .filter(c -> checkIfAbleToChangeState(c, carState))
                .isPresent();

        if (!isCarPartOfActiveReservation && isAbleToChangeState) {
            carDao.changeCarState(carId, CarStateEnum.stateOf(carState).orElseThrow(IllegalStateException::new));

            TransactionManager.commit();
            return true;
        } else {
            TransactionManager.rollback();
            return false;
        }
    }

    private boolean checkIfAbleToChangeState(Car car, String carState) {
        if (carState.equalsIgnoreCase(UNAVAILABLE.getState().toString())) {
            return car.makeUnavailable();
        }
        if (carState.equalsIgnoreCase(AVAILABLE.getState().toString())) {
            return car.makeAvailable();
        }
        return false;
    }
}
