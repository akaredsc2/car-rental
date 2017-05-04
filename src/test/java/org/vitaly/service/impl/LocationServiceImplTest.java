package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.location.Location;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by vitaly on 2017-04-20.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MysqlDaoFactory.class, TransactionManager.class})
@PowerMockIgnore("javax.management.*")
public class LocationServiceImplTest {
    private TransactionManager transactionManager = mock(TransactionManager.class);
    private MysqlDaoFactory daoFactory = mock(MysqlDaoFactory.class);
    private LocationDao locationDao = mock(LocationDao.class);
    private LocationService locationService = new LocationServiceImpl();

    @Test
    public void addNewLocation() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setState("state");
        locationDto.setCity("city");
        locationDto.setStreet("street");
        locationDto.setBuilding("building");
        locationDto.setCarDtoList(Collections.emptyList());

        stab();
        when(locationDao.create(any())).thenReturn(Optional.empty());
        locationService.addNewLocation(locationDto);

        InOrder inOrder = Mockito.inOrder(locationDao, transactionManager);
        inOrder.verify(locationDao).create(any());
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }

    private void stab() {
        PowerMockito.mockStatic(TransactionManager.class);
//        PowerMockito.when(TransactionManager.startTransaction()).thenReturn(transactionManager);
        PowerMockito.mockStatic(MysqlDaoFactory.class);
        PowerMockito.when(MysqlDaoFactory.getInstance()).thenReturn(daoFactory);
        when(daoFactory.getLocationDao()).thenReturn(locationDao);
    }

    @Test
    public void findLocationOfCar() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setId(23);

        stab();
        when(locationDao.findLocationByCarId(carDto.getId())).thenReturn(Optional.empty());
        locationService.findLocationOfCar(carDto);

        verify(locationDao).findLocationByCarId(carDto.getId());
    }

    @Test
    public void getAllLocationsMatchingPredicate() throws Exception {
        stab();
        locationService.getAll();

        verify(locationDao).getAll();
    }

    @Test
    public void changeLocationPhotoUrl() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(10);
        String photoUrl = "photoUrl";
        Location location = new Location.Builder()
                .setPhotoUrl(photoUrl)
                .build();

        stab();
        locationService.changeLocationPhotoUrl(locationDto, photoUrl);

        InOrder inOrder = Mockito.inOrder(locationDao, transactionManager);
        inOrder.verify(locationDao).update(locationDto.getId(), location);
        inOrder.verify(transactionManager).commit();
//        inOrder.verify(transactionManager).close();
    }
}
