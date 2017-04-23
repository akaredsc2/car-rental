package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.impl.dto.CarModelDto;

/**
 * Created by vitaly on 23.04.17.
 */
public class CarModelDtoMapper implements DtoMapper<CarModel, CarModelDto> {

    @Override
    public CarModel mapDtoToEntity(CarModelDto dto) {
        return new CarModel.Builder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setPhotoUrl(dto.getPhotoUrl())
                .setDoorCount(dto.getDoorCount())
                .setSeatCount(dto.getSeatCount())
                .setHorsePowerCount(dto.getHorsePowerCount())
                .build();
    }

    @Override
    public CarModelDto mapEntityToDto(CarModel entity) {
        CarModelDto carModelDto = new CarModelDto();

        carModelDto.setId(entity.getId());
        carModelDto.setName(entity.getName());
        carModelDto.setPhotoUrl(entity.getPhotoUrl());
        carModelDto.setDoorCount(entity.getDoorCount());
        carModelDto.setSeatCount(entity.getSeatCount());
        carModelDto.setHorsePowerCount(entity.getHorsePowerCount());

        return carModelDto;
    }
}
