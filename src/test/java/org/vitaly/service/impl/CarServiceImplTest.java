package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {
    @Mock
    private CarDao carDao;

    @InjectMocks
    private MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

    private CarService service = new CarServiceImpl();

    @Test
    public void successfulAddingOfCarModelReturnsTrue() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarDto carDto = new CarDto();
        carDto.setCarModelDto(carModelDto);

        when(carDao.create(any())).thenReturn(Optional.of(10L));
        boolean isCarAdded = service.addNewCar(carDto);

        assertTrue(isCarAdded);
    }

    @Test
    public void failedAddingOfCarModelReturnsFalse() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarDto carDto = new CarDto();
        carDto.setCarModelDto(carModelDto);

        when(carDao.create(any())).thenReturn(Optional.empty());
        boolean isCarAdded = service.addNewCar(carDto);

        assertFalse(isCarAdded);
    }

    @Test
    public void updatingCarThatIsNotInAvailableOrUnavailableStateReturnsFalse() throws Exception {
        CarDto carDto = new CarDto();
        Car car = new Car.Builder()
                .setState(CarStateEnum.RESERVED.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        boolean isCarUpdated = service.updateCar(carDto);

        assertFalse(isCarUpdated);
    }

    @Test
    public void updatingNonExistingCarReturnsFalse() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarDto carDto = new CarDto();
        carDto.setCarModelDto(carModelDto);
        Car car = new Car.Builder()
                .setState(CarStateEnum.AVAILABLE.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.update(anyLong(), any())).thenReturn(0);
        boolean isCarUpdated = service.updateCar(carDto);

        assertFalse(isCarUpdated);
    }

    @Test
    public void updatingExistingCarThatIsInAvailableOrUnavailableStateReturnsTrue() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarDto carDto = new CarDto();
        carDto.setCarModelDto(carModelDto);
        Car car = new Car.Builder()
                .setState(CarStateEnum.AVAILABLE.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.update(anyLong(), any())).thenReturn(1);
        boolean isCarUpdated = service.updateCar(carDto);

        assertTrue(isCarUpdated);
    }

    @Test
    public void movingCarThatIsNotInUnavailableStateReturnsFalse() throws Exception {
        CarDto carDto = new CarDto();
        LocationDto locationDto = new LocationDto();
        Car car = new Car.Builder()
                .setState(CarStateEnum.RESERVED.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        boolean isCarMoved = service.moveCarToLocation(carDto, locationDto);

        assertFalse(isCarMoved);
    }

    @Test
    public void movingNonExistingCarReturnsFalse() throws Exception {
        CarDto carDto = new CarDto();
        LocationDto locationDto = new LocationDto();
        Car car = new Car.Builder()
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.moveCarToLocation(anyLong(), anyLong())).thenReturn(false);
        boolean isCarMoved = service.moveCarToLocation(carDto, locationDto);

        assertFalse(isCarMoved);
    }

    @Test
    public void movingExistingCarThatIsInUnavailableStateReturnsTrue() throws Exception {
        CarDto carDto = new CarDto();
        LocationDto locationDto = new LocationDto();
        Car car = new Car.Builder()
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .build();

        when(carDao.findById(anyLong())).thenReturn(Optional.of(car));
        when(carDao.moveCarToLocation(anyLong(), anyLong())).thenReturn(true);
        boolean isCarMoved = service.moveCarToLocation(carDto, locationDto);

        assertTrue(isCarMoved);
    }
}
