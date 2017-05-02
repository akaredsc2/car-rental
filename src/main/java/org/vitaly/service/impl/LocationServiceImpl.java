package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.Transaction;
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
        try (Transaction transaction = Transaction.startTransaction()) {
            Location location = DtoMapperFactory.getInstance()
                    .getLocationDtoMapper()
                    .mapDtoToEntity(locationDto);

            LocationDao locationDao = MysqlDaoFactory.getInstance().getLocationDao();
            boolean isLocationCreated = locationDao.create(location).isPresent();

            transaction.commit();

            return isLocationCreated;
        }
    }

    @Override
    public Optional<LocationDto> findLocationOfCar(CarDto carDto) {
        DtoMapper<Location, LocationDto> mapper = DtoMapperFactory.getInstance().getLocationDtoMapper();

        long carId = carDto.getId();
        LocationDao locationDao = MysqlDaoFactory.getInstance().getLocationDao();
        return locationDao.findLocationByCarId(carId)
                .map(mapper::mapEntityToDto);
    }

    @Override
    public List<LocationDto> getAll() {
        DtoMapper<Location, LocationDto> mapper = DtoMapperFactory.getInstance().getLocationDtoMapper();

        LocationDao locationDao = MysqlDaoFactory.getInstance().getLocationDao();
        return locationDao.getAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean changeLocationPhotoUrl(LocationDto locationDto, String photoUrl) {
        try (Transaction transaction = Transaction.startTransaction()) {
            long locationId = locationDto.getId();
            Location locationWithPhoto = new Location.Builder()
                    .setPhotoUrl(photoUrl)
                    .build();

            LocationDao locationDao = MysqlDaoFactory.getInstance().getLocationDao();
            boolean isChanged = locationDao.update(locationId, locationWithPhoto) > 0;

            transaction.commit();

            return isChanged;
        }
    }
}
