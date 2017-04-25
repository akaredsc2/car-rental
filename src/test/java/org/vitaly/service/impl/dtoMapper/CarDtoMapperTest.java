package org.vitaly.service.impl.dtoMapper;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarState;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.CarModelDto;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 23.04.17.
 */
public class CarDtoMapperTest {
    private DtoMapper<Car, CarDto> mapper = DtoMapperFactory.getInstance().getCarDtoMapper();
    private CarDto expectedCarDto;
    private Car expectedCar;

    @Before
    public void setUp() throws Exception {
        long id = 3;
        CarModel carModel = CarModel.createDummyCarModelWithId(4);
        CarModelDto carModelDto = new CarModelDtoMapper().mapEntityToDto(carModel);
        CarState carState = CarStateEnum.AVAILABLE.getState();
        String registrationPlate = "3";
        String color = "three";
        BigDecimal pricePerHour = BigDecimal.TEN;

        expectedCarDto = new CarDto();
        expectedCarDto.setId(id);
        expectedCarDto.setCarModelDto(carModelDto);
        expectedCarDto.setState(carState);
        expectedCarDto.setRegistrationPlate(registrationPlate);
        expectedCarDto.setColor(color);
        expectedCarDto.setPricePerDay(pricePerHour);

        expectedCar = new Car.Builder()
                .setId(id)
                .setCarModel(carModel)
                .setState(carState)
                .setRegistrationPlate(registrationPlate)
                .setColor(color)
                .setPricePerDay(pricePerHour)
                .build();
    }

    @Test
    public void mapDtoToEntity() throws Exception {
        Car actualCar = mapper.mapDtoToEntity(expectedCarDto);

        assertThat(actualCar, allOf(
                equalTo(expectedCar),
                hasId(expectedCar.getId())));
    }

    @Test
    public void mapEntityToDto() throws Exception {
        CarDto actualCarDto = mapper.mapEntityToDto(expectedCar);

        assertEquals(expectedCarDto, actualCarDto);
    }
}