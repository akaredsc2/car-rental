package org.vitaly.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by vitaly on 02.05.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationServiceImplTest {
    @Mock
    private LocationDao locationDao;

    @InjectMocks
    private MysqlDaoFactory daoFactory = MysqlDaoFactory.getInstance();

    private LocationService service = new LocationServiceImpl();

    @Test
    public void successfulAddingOfLocationReturnsTrue() throws Exception {
        LocationDto locationDto = new LocationDto();

        when(locationDao.create(any())).thenReturn(Optional.of(10L));
        boolean isLocationAdded = service.addNewLocation(locationDto);

        assertTrue(isLocationAdded);
    }

    @Test
    public void failedAddingOfLocationReturnsFalse() throws Exception {
        LocationDto locationDto = new LocationDto();

        when(locationDao.create(any())).thenReturn(Optional.empty());
        boolean isLocationAdded = service.addNewLocation(locationDto);

        assertFalse(isLocationAdded);
    }

    @Test
    public void successfulUpdateOfLocationReturnsTrue() throws Exception {
        LocationDto locationDto = new LocationDto();

        when(locationDao.update(anyLong(), any())).thenReturn(1);
        boolean isCarModelUpdated = service.updateLocation(locationDto, "new photo url");

        assertTrue(isCarModelUpdated);
    }

    @Test
    public void failedUpdateOfLocationReturnsFalse() throws Exception {
        LocationDto locationDto = new LocationDto();

        when(locationDao.update(anyLong(), any())).thenReturn(0);
        boolean isCarModelUpdated = service.updateLocation(locationDto, "new photo url");

        assertFalse(isCarModelUpdated);
    }
}