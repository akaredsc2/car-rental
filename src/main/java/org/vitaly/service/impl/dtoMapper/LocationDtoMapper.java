package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.location.Location;
import org.vitaly.service.impl.dto.LocationDto;

/**
 * Created by vitaly on 23.04.17.
 */
public class LocationDtoMapper implements DtoMapper<Location, LocationDto> {

    @Override
    public Location mapDtoToEntity(LocationDto dto) {
        return new Location.Builder()
                .setId(dto.getId())
                .setState(dto.getState())
                .setCity(dto.getCity())
                .setStreet(dto.getStreet())
                .setBuilding(dto.getBuilding())
                .setPhotoUrl(dto.getPhotoUrl())
                .build();
    }

    @Override
    public LocationDto mapEntityToDto(Location entity) {
        LocationDto locationDto = new LocationDto();

        locationDto.setId(entity.getId());
        locationDto.setState(entity.getState());
        locationDto.setCity(entity.getCity());
        locationDto.setStreet(entity.getStreet());
        locationDto.setBuilding(entity.getBuilding());
        locationDto.setPhotoUrl(entity.getPhotoUrl());

        return locationDto;
    }
}
