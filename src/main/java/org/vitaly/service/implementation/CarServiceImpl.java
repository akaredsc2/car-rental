package org.vitaly.service.implementation;

import org.vitaly.dao.abstraction.CarDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.model.car.Car;
import org.vitaly.service.abstraction.CarService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//import org.vitaly.template.abstraction.DaoFactory;

/**
 * Created by vitaly on 2017-04-10.
 */
public class CarServiceImpl implements CarService {
    private CarDao dao;

    public CarServiceImpl(PooledConnection connection) {
//        this.template = DaoFactory.getMysqlDaoFactory().createCarDao(connection);
    }

    @Override
    public Optional<Car> findById(long id) {
        return dao.findById(id);
    }

    @Override
    public Optional<Long> findIdOfEntity(Car car) {
        return dao.findIdOfEntity(car);
    }

    @Override
    public List<Car> getAll() {
        return dao.getAll();
    }

    @Override
    public Optional<Long> create(Car car) {
        return dao.create(car);
    }

    @Override
    public int update(long id, Car car) {
        return dao.update(id, car);
    }

    @Override
    public boolean addCarToLocation(long carId, long locationId) {
        return dao.addCarToLocation(carId, locationId);
    }

    @Override
    public List<Car> findCarsAtLocation(long locationId) {
        return dao.findCarsAtLocation(locationId);
    }

    @Override
    public List<Car> findCarsByModel(String model) {
        return dao.findCarsByModel(model);
    }

    @Override
    public List<Car> findCarsWithPriceBetween(BigDecimal from, BigDecimal to) {
        return dao.findCarsWithPriceBetween(from, to);
    }

    @Override
    public List<String> findAllCarModels() {
        return dao.findAllCarModels();
    }
}
