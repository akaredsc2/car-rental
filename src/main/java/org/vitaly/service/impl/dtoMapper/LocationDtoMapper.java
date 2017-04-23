package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.car.Car;
import org.vitaly.model.location.Location;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 23.04.17.
 */
public class LocationDtoMapper implements DtoMapper<Location, LocationDto> {

    @Override
    public Location mapDtoToEntity(LocationDto dto) {
        List<Car> cars = dto.getCarDtoList()
                .stream()
                .map(carDto -> Car.createDummyCarWithId(carDto.getId()))
                .collect(Collectors.toList());

        return new Location.Builder()
                .setId(dto.getId())
                .setState(dto.getState())
                .setCity(dto.getCity())
                .setStreet(dto.getStreet())
                .setBuilding(dto.getBuilding())
                .setPhotoUrl(dto.getPhotoUrl())
                .setCars(cars)
                .build();
    }

    @Override
    public LocationDto mapEntityToDto(Location entity) {
        CarDtoMapper carDtoMapper = new CarDtoMapper();

        List<CarDto> carDtoList = entity.getCars()
                .stream()
                .map(carDtoMapper::mapEntityToDto)
                .collect(Collectors.toList());

        LocationDto locationDto = new LocationDto();

        locationDto.setId(entity.getId());
        locationDto.setState(entity.getState());
        locationDto.setCity(entity.getCity());
        locationDto.setStreet(entity.getStreet());
        locationDto.setBuilding(entity.getBuilding());
        locationDto.setPhotoUrl(entity.getPhotoUrl());
        locationDto.setCarDtoList(carDtoList);

        return locationDto;
    }
}
