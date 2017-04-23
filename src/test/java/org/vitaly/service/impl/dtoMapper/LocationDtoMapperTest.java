package org.vitaly.service.impl.dtoMapper;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.model.car.Car;
import org.vitaly.model.location.Location;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.LocationDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 23.04.17.
 */
public class LocationDtoMapperTest {
    private DtoMapper<Location, LocationDto> mapper = new LocationDtoMapper();
    private LocationDto expectedLocationDto;
    private Location expectedLocation;

    @Before
    public void setUp() throws Exception {
        long id = 5;
        String state = "kiev region";
        String city = "kotsjubinske";
        String street = "ponomarjova";
        String building = "14";
        String photoUrl = "url3";
        List<Car> carList = Arrays.asList(
                Car.createDummyCarWithId(6),
                Car.createDummyCarWithId(7));

        CarDtoMapper carDtoMapper = new CarDtoMapper();
        List<CarDto> carDtoList = carList
                .stream()
                .map(carDtoMapper::mapEntityToDto)
                .collect(Collectors.toList());

        expectedLocationDto = new LocationDto();
        expectedLocationDto.setId(id);
        expectedLocationDto.setState(state);
        expectedLocationDto.setCity(city);
        expectedLocationDto.setStreet(street);
        expectedLocationDto.setBuilding(building);
        expectedLocationDto.setPhotoUrl(photoUrl);
        expectedLocationDto.setCarDtoList(carDtoList);

        expectedLocation = new Location.Builder()
                .setId(id)
                .setState(state)
                .setCity(city)
                .setStreet(street)
                .setBuilding(building)
                .setPhotoUrl(photoUrl)
                .setCars(carList)
                .build();
    }

    @Test
    public void mapDtoToEntity() throws Exception {
        Location actualLocation = mapper.mapDtoToEntity(expectedLocationDto);

        assertThat(actualLocation, allOf(
                equalTo(expectedLocation),
                hasId(expectedLocation.getId())));
    }

    @Test
    public void mapEntityToDto() throws Exception {
        LocationDto actualLocationDto = mapper.mapEntityToDto(expectedLocation);

        assertEquals(expectedLocationDto, actualLocationDto);
    }
}