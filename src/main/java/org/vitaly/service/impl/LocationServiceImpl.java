package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.abstraction.transaction.Transaction;
import org.vitaly.model.location.Location;
import org.vitaly.service.abstraction.LocationService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-10.
 */
public class LocationServiceImpl implements LocationService {
    private TransactionFactory transactionFactory;

    public LocationServiceImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public void addNewLocation(LocationDto locationDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            Location location = new Location.Builder()
                    .setState(locationDto.getState())
                    .setCity(locationDto.getCity())
                    .setStreet(locationDto.getStreet())
                    .setBuilding(locationDto.getBuilding())
                    .build();

            LocationDao locationDao = transaction.getLocationDao();
            locationDao.create(location);

            transaction.commit();
        }
    }

    @Override
    public Optional<Location> findLocationOfCar(CarDto carDto) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            long carId = carDto.getId();
            LocationDao locationDao = transaction.getLocationDao();
            return locationDao.findLocationByCarId(carId);
        }
    }

    @Override
    public List<Location> getAllMatchingLocations(Predicate<Location> locationPredicate) {
        try (Transaction transaction = transactionFactory.createTransaction()) {
            LocationDao locationDao = transaction.getLocationDao();
            return locationDao.getAll()
                    .stream()
                    .filter(locationPredicate)
                    .collect(Collectors.toList());
        }
    }
}
