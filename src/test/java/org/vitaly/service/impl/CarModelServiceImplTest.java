package org.vitaly.service.impl;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarModelDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private CarModelDao carModelDao = mock(CarModelDao.class);
    private CarModelService carModelService = new CarModelServiceImpl(transactionFactory);

    @Test
    public void addCarModel() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setName("name");
        carModelDto.setSeatCount(4);
        carModelDto.setDoorCount(4);
        carModelDto.setHorsePowerCount(200);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarModelDao()).thenReturn(carModelDao);
        carModelService.addCarModel(carModelDto);

        InOrder inOrder = Mockito.inOrder(transaction, carModelDao);
        inOrder.verify(carModelDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void getAllMatchingCarModels() throws Exception {
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarModelDao()).thenReturn(carModelDao);
        carModelService.getAllMatchingCarModels(x -> true);

        InOrder inOrder = Mockito.inOrder(transaction, carModelDao);
        inOrder.verify(carModelDao).getAll();
        inOrder.verify(transaction).close();
    }

    @Test
    public void updateCarModel() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(10);
        carModelDto.setName("");
        carModelDto.setPhotoUrl("url");

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarModelDao()).thenReturn(carModelDao);
        carModelService.updateCarModel(carModelDto);

        InOrder inOrder = Mockito.inOrder(transaction, carModelDao);
        inOrder.verify(carModelDao).update(eq(carModelDto.getId()), any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void findCarsWithPhotos() throws Exception {
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getCarModelDao()).thenReturn(carModelDao);
        carModelService.findCarsWithPhotos();

        InOrder inOrder = Mockito.inOrder(transaction, carModelDao);
        inOrder.verify(carModelDao).findCarsWithPhotos();
        inOrder.verify(transaction).close();
    }
}