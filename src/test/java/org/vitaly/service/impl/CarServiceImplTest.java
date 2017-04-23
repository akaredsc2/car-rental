package org.vitaly.service.impl;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.service.abstraction.CarService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-20.
 */
public class CarServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private CarDao carDao = mock(CarDao.class);
    private CarService carService = new CarServiceImpl(transactionFactory);

    @Test
    public void findCarsAtLocation() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(798);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarDao()).thenReturn(carDao);
        carService.findCarsAtLocation(locationDto);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).findCarsAtLocation(locationDto.getId());
        inOrder.verify(transaction).close();
    }

    @Test
    public void findCarsByModel() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(79);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarDao()).thenReturn(carDao);
        carService.findCarsByModel(carModelDto);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).findCarsByModel(carModelDto.getId());
        inOrder.verify(transaction).close();
    }

    @Test
    public void findCarsWithPriceBetween() throws Exception {
        BigDecimal from = BigDecimal.ONE;
        BigDecimal to = BigDecimal.TEN;

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarDao()).thenReturn(carDao);
        carService.findCarsWithPriceBetween(from, to);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).findCarsWithPriceBetween(from, to);
        inOrder.verify(transaction).close();
    }

    @Test
    public void getAllMatchingCars() throws Exception {
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarDao()).thenReturn(carDao);
        carService.getAllMatchingCars(x -> true);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).getAll();
        inOrder.verify(transaction).close();
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

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarDao()).thenReturn(carDao);
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

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarDao()).thenReturn(carDao);
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

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarDao()).thenReturn(carDao);
        carService.moveCarToLocation(carDto, locationDto);

        InOrder inOrder = Mockito.inOrder(carDao, transaction);
        inOrder.verify(carDao).moveCarToLocation(carDto.getId(), locationDto.getId());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}
