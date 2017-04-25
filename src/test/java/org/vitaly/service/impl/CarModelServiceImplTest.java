package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarModelDto;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-22.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MysqlDaoFactory.class)
@PowerMockIgnore("javax.management.*")
public class CarModelServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private CarModelDao carModelDao = mock(CarModelDao.class);
    private CarModelService carModelService = new CarModelServiceImpl(transactionFactory);

    @Test
    public void addCarModel() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setName("name");
        carModelDto.setSeatCount(4);
        carModelDto.setDoorCount(4);
        carModelDto.setHorsePowerCount(200);

        stab();
        when(carModelDao.create(any())).thenReturn(Optional.empty());
        carModelService.addCarModel(carModelDto);

        InOrder inOrder = Mockito.inOrder(transaction, carModelDao);
        inOrder.verify(carModelDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    private void stab() {
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(daoFactory.getCarModelDao()).thenReturn(carModelDao);
    }

    @Test
    public void getAllMatchingCarModels() throws Exception {
        stab();
        carModelService.getAllMatchingCarModels(x -> true);

        verify(carModelDao).getAll();
    }

    @Test
    public void updateCarModel() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setId(10);
        carModelDto.setName("");
        carModelDto.setPhotoUrl("url");

        stab();
        carModelService.updateCarModel(carModelDto);

        InOrder inOrder = Mockito.inOrder(transaction, carModelDao);
        inOrder.verify(carModelDao).update(eq(carModelDto.getId()), any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void findCarsWithPhotos() throws Exception {
        stab();
        carModelService.findCarsWithPhotos();

        verify(carModelDao).findCarsWithPhotos();
    }
}