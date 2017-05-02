package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarDto;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 02.05.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CarModelServiceImplTestWithInjectMocks {
    @Mock
    private CarModelDao carModelDao;

    @InjectMocks
    private MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

    @Test
    public void findModelOfCar() throws Exception {
        int carId = 10;
        CarDto carDto = new CarDto();
        carDto.setId(carId);
        CarModelService service = new CarModelServiceImpl();

        when(carModelDao.findModelOfCar(carId)).thenReturn(Optional.empty());
        service.findModelOfCar(carDto);

        verify(carModelDao).findModelOfCar(carId);
    }
}