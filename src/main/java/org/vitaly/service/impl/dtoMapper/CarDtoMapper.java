package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.car.Car;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;

/**
 * Created by vitaly on 23.04.17.
 */
public class CarDtoMapper implements DtoMapper<Car, CarDto> {

    @Override
    public Car mapDtoToEntity(CarDto dto) {
        // TODO: 23.04.17 replace with factory
        CarModelDto carModelDto = dto.getCarModelDto();
        CarModel carModel = new CarModelDtoMapper().mapDtoToEntity(carModelDto);

        return new Car.Builder()
                .setId(dto.getId())
                .setCarModel(carModel)
                .setState(dto.getState())
                .setRegistrationPlate(dto.getRegistrationPlate())
                .setColor(dto.getColor())
                .setPricePerDay(dto.getPricePerDay())
                .build();
    }

    @Override
    public CarDto mapEntityToDto(Car entity) {

        // TODO: 23.04.17 replace with factory
        CarModelDto carModelDto = new CarModelDtoMapper().mapEntityToDto(entity.getCarModel());

        CarDto carDto = new CarDto();
        carDto.setId(entity.getId());
        carDto.setCarModelDto(carModelDto);
        carDto.setState(entity.getState());
        carDto.setRegistrationPlate(entity.getRegistrationPlate());
        carDto.setColor(entity.getColor());
        carDto.setPricePerDay(entity.getPricePerDay());

        return carDto;
    }
}
