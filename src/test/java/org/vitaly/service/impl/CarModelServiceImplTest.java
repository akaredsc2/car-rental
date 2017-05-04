package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 02.05.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CarModelServiceImplTest {
    @Mock
    private CarModelDao carModelDao;

    @InjectMocks
    private MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

    private CarModelService service = new CarModelServiceImpl();

    @Test
    public void successfulAddingOfCarModelReturnsTrue() throws Exception {
        CarModelDto carModelDto = new CarModelDto();

        when(carModelDao.create(any())).thenReturn(Optional.of(10L));
        boolean isCarModelAdded = service.addCarModel(carModelDto);

        assertTrue(isCarModelAdded);
    }

    @Test
    public void failedAddingOfCarModelReturnsFalse() throws Exception {
        CarModelDto carModelDto = new CarModelDto();

        when(carModelDao.create(any())).thenReturn(Optional.empty());
        boolean isCarModelAdded = service.addCarModel(carModelDto);

        assertFalse(isCarModelAdded);
    }

    @Test
    public void successfulUpdateOfCarModelReturnsTrue() throws Exception {
        CarModelDto carModelDto = new CarModelDto();

        when(carModelDao.update(anyLong(), any())).thenReturn(1);
        boolean isCarModelUpdated = service.updateCarModel(carModelDto);

        assertTrue(isCarModelUpdated);
    }

    @Test
    public void failedUpdateOfCarModelReturnsFalse() throws Exception {
        CarModelDto carModelDto = new CarModelDto();

        when(carModelDao.update(anyLong(), any())).thenReturn(0);
        boolean isCarModelUpdated = service.updateCarModel(carModelDto);

        assertFalse(isCarModelUpdated);
    }
}