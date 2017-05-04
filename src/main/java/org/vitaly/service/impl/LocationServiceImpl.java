package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.location.Location;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class LocationServiceImpl implements LocationService {

    @Override
    public boolean addNewLocation(LocationDto locationDto) {
        TransactionManager.startTransaction();

        Location location = DtoMapperFactory.getInstance()
                .getLocationDtoMapper()
                .mapDtoToEntity(locationDto);

        boolean isLocationCreated = MysqlDaoFactory.getInstance()
                .getLocationDao()
                .create(location)
                .isPresent();

        TransactionManager.commit();

        return isLocationCreated;
    }

    @Override
    public Optional<LocationDto> findLocationOfCar(CarDto carDto) {
        DtoMapper<Location, LocationDto> mapper = DtoMapperFactory.getInstance().getLocationDtoMapper();

        long carId = carDto.getId();
        return MysqlDaoFactory.getInstance()
                .getLocationDao()
                .findLocationByCarId(carId)
                .map(mapper::mapEntityToDto);
    }

    @Override
    public List<LocationDto> getAll() {
        DtoMapper<Location, LocationDto> mapper = DtoMapperFactory.getInstance().getLocationDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getLocationDao()
                .getAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateLocation(LocationDto locationDto, String photoUrl) {
        TransactionManager.startTransaction();

        long locationId = locationDto.getId();
        Location locationWithPhoto = new Location.Builder()
                .setPhotoUrl(photoUrl)
                .build();

        LocationDao locationDao = MysqlDaoFactory.getInstance().getLocationDao();
        boolean isChanged = locationDao.update(locationId, locationWithPhoto) > 0;

        TransactionManager.commit();

        return isChanged;
    }
}
