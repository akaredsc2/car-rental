package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.location.Location;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class LocationServiceImpl implements LocationService {
    private TransactionFactory transactionFactory;
    private DtoMapperFactory dtoMapperFactory;

    LocationServiceImpl(TransactionFactory transactionFactory) {
        this(transactionFactory, new DtoMapperFactory());
    }

    public LocationServiceImpl(TransactionFactory transactionFactory, DtoMapperFactory dtoMapperFactory) {
        this.transactionFactory = transactionFactory;
        this.dtoMapperFactory = dtoMapperFactory;
    }

    @Override
    public boolean addNewLocation(LocationDto locationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Location location = dtoMapperFactory.getLocationDtoMapper().mapDtoToEntity(locationDto);

            LocationDao locationDao = transaction.getLocationDao();
            boolean isLocationCreated = locationDao.create(location).isPresent();

            transaction.commit();

            return isLocationCreated;
        }
    }

    @Override
    public Optional<LocationDto> findLocationOfCar(CarDto carDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Location, LocationDto> mapper = dtoMapperFactory.getLocationDtoMapper();

            long carId = carDto.getId();
            LocationDao locationDao = transaction.getLocationDao();
            return locationDao.findLocationByCarId(carId)
                    .map(mapper::mapEntityToDto);
        }
    }

    @Override
    public List<LocationDto> getAllMatchingLocations(Predicate<Location> locationPredicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            DtoMapper<Location, LocationDto> mapper = dtoMapperFactory.getLocationDtoMapper();

            LocationDao locationDao = transaction.getLocationDao();
            return locationDao.getAll()
                    .stream()
                    .filter(locationPredicate)
                    .map(mapper::mapEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean changeLocationPhotoUrl(LocationDto locationDto, String photoUrl) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long locationId = locationDto.getId();
            LocationDao locationDao = transaction.getLocationDao();
            boolean isChanged = locationDao.changeImageUrl(locationId, photoUrl);

            transaction.commit();

            return isChanged;
        }
    }
}
