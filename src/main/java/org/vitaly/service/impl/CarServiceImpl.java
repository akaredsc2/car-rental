package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.vitaly.model.car.CarStateEnum.*;

/**
 * Created by vitaly on 2017-04-10.
 */
public class CarServiceImpl implements CarService {

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
    @Override
    public Optional<CarDto> findCarById(long carId) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getCarDao()
                .findById(carId)
                .map(mapper::mapEntityToDto);
    }

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
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

    /**
     * @inheritDoc
     */
    @Override
    public boolean updateCar(CarDto carDto) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();
        boolean isAbleToBeUpdated = carDao.findById(carDto.getId())
                .map(Car::getState)
                .filter(state -> state == UNAVAILABLE.getState())
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

    /**
     * @inheritDoc
     */
    @Override
    public boolean moveCarToLocation(CarDto carDto, LocationDto locationDto) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_REPEATABLE_READ);

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

    /**
     * @inheritDoc
     */
    @Override
    public boolean changeCarState(CarDto carDto, String carState) {
        TransactionManager.startTransactionWithIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        long carId = carDto.getId();
        CarDao carDao = MysqlDaoFactory.getInstance().getCarDao();

        boolean isAbleToChangeState = carDao
                .findById(carId)
                .filter(c -> checkIfAbleToChangeState(c, carState))
                .isPresent();

        if (isAbleToChangeState) {
            boolean isStateChanged = doChangeState(carState, carId, carDao);

            TransactionManager.commit();
            return isStateChanged;
        } else {
            TransactionManager.rollback();
            return false;
        }
    }

    private boolean doChangeState(String carState, long carId, CarDao carDao) {
        boolean isStateChanged;
        CarState state = CarStateEnum.valueOf(carState.toUpperCase()).getState();

        if (carState.equalsIgnoreCase(CarStateEnum.RETURNED.toString())) {
            isStateChanged = carDao.changeCarState(carId, state);
        } else {
            boolean isCarPartOfActiveReservation = MysqlDaoFactory.getInstance()
                    .getReservationDao()
                    .isCarPartOfActiveReservations(carId);

            isStateChanged = !isCarPartOfActiveReservation && carDao.changeCarState(carId, state);
        }
        return isStateChanged;
    }

    private boolean checkIfAbleToChangeState(Car car, String carState) {
        if (carState.equalsIgnoreCase(UNAVAILABLE.getState().toString())) {
            return car.makeUnavailable();
        }
        if (carState.equalsIgnoreCase(AVAILABLE.getState().toString())) {
            return car.makeAvailable();
        }
        if (carState.equalsIgnoreCase(RETURNED.getState().toString())) {
            return car.doReturn();
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Optional<CarDto> findCarForReservation(ReservationDto reservationDto) {
        DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getCarDao()
                .findCarByReservation(reservationDto.getId())
                .map(mapper::mapEntityToDto);
    }
}
