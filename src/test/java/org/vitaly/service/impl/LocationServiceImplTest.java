package org.vitaly.service.impl;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 2017-04-20.
 */
public class LocationServiceImplTest {
    private TransactionFactory transactionFactory = mock(TransactionFactory.class);
    private Transaction transaction = mock(Transaction.class);
    private LocationDao locationDao = mock(LocationDao.class);
    private LocationService locationService = new LocationServiceImpl(transactionFactory);

    @Test
    public void addNewLocation() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setState("state");
        locationDto.setCity("city");
        locationDto.setStreet("street");
        locationDto.setBuilding("building");
        locationDto.setCarDtoList(Collections.emptyList());

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getLocationDao()).thenReturn(locationDao);
        locationService.addNewLocation(locationDto);

        InOrder inOrder = Mockito.inOrder(locationDao, transaction);
        inOrder.verify(locationDao).create(any());
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }

    @Test
    public void findLocationOfCar() throws Exception {
        CarDto carDto = new CarDto();
        carDto.setId(23);

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getLocationDao()).thenReturn(locationDao);
        locationService.findLocationOfCar(carDto);

        InOrder inOrder = Mockito.inOrder(locationDao, transaction);
        inOrder.verify(locationDao).findLocationByCarId(carDto.getId());
        inOrder.verify(transaction).close();
    }

    @Test
    public void getAllLocationsMatchingPredicate() throws Exception {
        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getLocationDao()).thenReturn(locationDao);
        locationService.getAllMatchingLocations(x -> true);

        InOrder inOrder = Mockito.inOrder(locationDao, transaction);
        inOrder.verify(locationDao).getAll();
        inOrder.verify(transaction).close();
    }

    @Test
    public void changeLocationPhotoUrl() throws Exception {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(10);
        String photoUrl = "photoUrl";

        when(transactionFactory.createTransaction()).thenReturn(transaction);
        when(transaction.getLocationDao()).thenReturn(locationDao);
        locationService.changeLocationPhotoUrl(locationDto, photoUrl);

        InOrder inOrder = Mockito.inOrder(locationDao, transaction);
        inOrder.verify(locationDao).changeImageUrl(locationDto.getId(), photoUrl);
        inOrder.verify(transaction).commit();
        inOrder.verify(transaction).close();
    }
}
