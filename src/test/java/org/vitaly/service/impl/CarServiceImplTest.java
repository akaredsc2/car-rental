package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.Transaction;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MysqlDaoFactory.class, Transaction.class})
@PowerMockIgnore("javax.management.*")
public class CarServiceImplTest {
    private Transaction transaction = mock(Transaction.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private CarDao carDao = mock(CarDao.class);
    private CarService carService = new CarServiceImpl();

    @Test
    public void findCarsAtLocation() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(798);

        stab();
        carService.findCarsAtLocation(locationDto);

        verify(carDao).findCarsAtLocation(locationDto.getId());
    }

    private void stab() {
        PowerMockito.mockStatic(Transaction.class);
        PowerMockito.when(Transaction.startTransaction()).thenReturn(transaction);
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(daoFactory.getCarDao()).thenReturn(carDao);
    }

    @Test
    public void findCarsByModel() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(79);

        stab();
        carService.findCarsByModel(carModelDto);

        verify(carDao).findCarsByModel(carModelDto.getId());
    }

    @Test
    public void findCarsWithPriceBetween() throws Exception {
        BigDecimal from = BigDecimal.ONE;
        BigDecimal to = BigDecimal.TEN;

        stab();
        carService.findCarsWithPriceBetween(from, to);

        verify(carDao).findCarsWithPriceBetween(from, to);
    }

    @Test
    public void getAllMatchingCars() throws Exception {
        stab();
        carService.getAllCars();

        verify(carDao).getAll();
    }

    @Test
    public void addNewCar() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(809);

        CarDto carDto = new CarDto();
        carDto.setCarModelDto(carModelDto);
        carDto.setColor("red");
        carDto.setPricePerDay(BigDecimal.ZERO);
        carDto.setRegistrationPlate("registration plate");
        carDto.setState(CarStateEnum.AVAILABLE.getState());

        stab();
        when(carDao.create(any())).thenReturn(Optional.empty());
        carService.addNewCar(carDto);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void updateCar() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(809);

        CarDto carDto = new CarDto();
        carDto.setId(798);
        carDto.setCarModelDto(carModelDto);
        carDto.setColor("black");
        carDto.setPricePerDay(BigDecimal.ZERO);
        carDto.setRegistrationPlate("not a registration plate");
        carDto.setState(CarStateEnum.AVAILABLE.getState());

        stab();
        carService.updateCar(carDto);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).update(eq(carDto.getId()), any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void moveCarToLocation() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(1);
        CarDto carDto = new CarDto();
        carDto.setId(2);

        stab();
        carService.moveCarToLocation(carDto, locationDto);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).moveCarToLocation(carDto.getId(), locationDto.getId());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}
