package org.vitaly.service.impl;

import org.vitaly.dao.abstraction.CarModelDao;
import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.Transaction;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelServiceImpl implements CarModelService {

    @Override
    public boolean addCarModel(CarModelDto carModelDto) {
        try (Transaction transaction = Transaction.startTransaction()) {
            CarModel carModel = DtoMapperFactory.getInstance()
                    .getCarModelDtoMapper()
                    .mapDtoToEntity(carModelDto);
            CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
            boolean isCreated = carModelDao.create(carModel).isPresent();

            transaction.commit();

            return isCreated;
        }
    }

    @Override
    public List<CarModelDto> getAllCarModels() {
        DtoMapper<CarModel, CarModelDto> mapper = DtoMapperFactory.getInstance().getCarModelDtoMapper();

        CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
        return carModelDao.getAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCarModel(CarModelDto carModelDto) {
        try (Transaction transaction = Transaction.startTransaction()) {
            CarModel carModel = DtoMapperFactory.getInstance()
                    .getCarModelDtoMapper()
                    .mapDtoToEntity(carModelDto);
            CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
            carModelDao.update(carModelDto.getId(), carModel);

            transaction.commit();
        }
    }

    @Override
    public List<CarModelDto> findCarsWithPhotos() {
        DtoMapper<CarModel, CarModelDto> mapper = DtoMapperFactory.getInstance().getCarModelDtoMapper();

        CarModelDao carModelDao = MysqlDaoFactory.getInstance().getCarModelDao();
        return carModelDao.findCarsWithPhotos()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }
}

