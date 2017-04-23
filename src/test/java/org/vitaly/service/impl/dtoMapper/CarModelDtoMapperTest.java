package org.vitaly.service.impl.dtoMapper;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.service.impl.dto.CarModelDto;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 23.04.17.
 */
public class CarModelDtoMapperTest {
    private DtoMapper<CarModel, CarModelDto> mapper = new CarModelDtoMapper();
    private CarModelDto expectedCarModelDto;
    private CarModel expectedCarModel;

    @Before
    public void setUp() throws Exception {
        int id = 2;
        String name = "2";
        String photoUrl = "url2";
        int doorCount = 2;
        int seatCount = 2;
        int horsePowerCount = 2;

        expectedCarModelDto = new CarModelDto();
        expectedCarModelDto.setId(id);
        expectedCarModelDto.setName(name);
        expectedCarModelDto.setPhotoUrl(photoUrl);
        expectedCarModelDto.setDoorCount(doorCount);
        expectedCarModelDto.setSeatCount(seatCount);
        expectedCarModelDto.setHorsePowerCount(horsePowerCount);

        expectedCarModel = new CarModel.Builder()
                .setId(id)
                .setName(name)
                .setPhotoUrl(photoUrl)
                .setDoorCount(doorCount)
                .setSeatCount(seatCount)
                .setHorsePowerCount(horsePowerCount)
                .build();
    }

    @Test
    public void mapDtoToEntity() throws Exception {
        CarModel actualCarModel = mapper.mapDtoToEntity(expectedCarModelDto);

        assertThat(actualCarModel, allOf(
                equalTo(expectedCarModel),
                hasId(expectedCarModel.getId())));
    }

    @Test
    public void mapEntityToDto() throws Exception {
        CarModelDto actualCarModelDto = mapper.mapEntityToDto(expectedCarModel);

        assertEquals(expectedCarModelDto, actualCarModelDto);
    }
}