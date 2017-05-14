package org.vitaly.service.impl;

import org.vitaly.dao.impl.mysql.factory.MysqlDaoFactory;
import org.vitaly.dao.impl.mysql.transaction.TransactionManager;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.abstraction.CarModelService;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.dtoMapper.DtoMapper;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelServiceImpl implements CarModelService {

    @Override
    public boolean addCarModel(CarModelDto carModelDto) {
        TransactionManager.startTransaction();

        CarModel carModel = DtoMapperFactory.getInstance()
                .getCarModelDtoMapper()
                .mapDtoToEntity(carModelDto);

        boolean isCreated = MysqlDaoFactory.getInstance()
                .getCarModelDao()
                .create(carModel)
                .isPresent();

        TransactionManager.commit();

        return isCreated;
    }

    @Override
    public List<CarModelDto> getAll() {
        DtoMapper<CarModel, CarModelDto> mapper = DtoMapperFactory.getInstance().getCarModelDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getCarModelDao()
                .getAll()
                .stream()
                .map(mapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateCarModel(CarModelDto carModelDto) {
        TransactionManager.startTransaction();

        CarModel carModel = DtoMapperFactory.getInstance()
                .getCarModelDtoMapper()
                .mapDtoToEntity(carModelDto);

        boolean isUpdated = MysqlDaoFactory.getInstance()
                .getCarModelDao()
                .update(carModelDto.getId(), carModel) > 0;

        TransactionManager.commit();

        return isUpdated;
    }

    @Override
    public Optional<CarModelDto> findModelOfCar(CarDto carDto) {
        long carId = carDto.getId();
        DtoMapper<CarModel, CarModelDto> mapper = DtoMapperFactory.getInstance().getCarModelDtoMapper();

        return MysqlDaoFactory.getInstance()
                .getCarModelDao()
                .findModelOfCar(carId)
                .map(mapper::mapEntityToDto);
    }
}

