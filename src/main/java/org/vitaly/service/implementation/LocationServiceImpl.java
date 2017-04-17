package org.vitaly.service.implementation;

import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
//import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.LocationDao;
import org.vitaly.model.location.Location;
import org.vitaly.service.abstraction.LocationService;

import java.util.List;
import java.util.Optional;

/**
 * Created by vitaly on 2017-04-10.
 */
public class LocationServiceImpl implements LocationService {
    private LocationDao dao;

    public LocationServiceImpl(PooledConnection connection) {
//        this.dao = DaoFactory.getMysqlDaoFactory().createLocationDao(connection);
    }

    @Override
    public Optional<Location> findById(long id) {
        return dao.findById(id);
    }

    @Override
    public Optional<Long> findIdOfEntity(Location location) {
        return dao.findIdOfEntity(location);
    }

    @Override
    public List<Location> getAll() {
        return dao.getAll();
    }

    @Override
    public Optional<Long> create(Location location) {
        return dao.create(location);
    }

    @Override
    public Optional<Location> findLocationByCarId(long carId) {
        return dao.findLocationByCarId(carId);
    }
}
