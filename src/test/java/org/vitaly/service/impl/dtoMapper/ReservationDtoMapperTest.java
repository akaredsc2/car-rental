package org.vitaly.service.impl.dtoMapper;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.reservation.ReservationState;
import org.vitaly.model.reservation.ReservationStateEnum;
import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 25.04.17.
 */
public class ReservationDtoMapperTest {
    private DtoMapper<Reservation, ReservationDto> mapper = DtoMapperFactory.getInstance().getReservationDtoMapper();
    private ReservationDto expectedReservationDto;
    private Reservation expectedReservation;

    @Before
    public void setUp() throws Exception {
        int id = 15;
        User client = User.createDummyClientWithId(1L);
        User admin = User.createDummyAdminWithId(2L);
        Car car = Car.createDummyCarWithId(3L);
        ReservationState state = ReservationStateEnum.REJECTED.getState();
        LocalDateTime pickUpDateTime = LocalDateTime.now();
        LocalDateTime dropOffDatetime = LocalDateTime.now().plusDays(1);
        String reason = "reason";

        UserDto clientDto = DtoMapperFactory.getInstance()
                .getUserDtoMapper()
                .mapEntityToDto(client);
        UserDto adminDto = DtoMapperFactory.getInstance()
                .getUserDtoMapper()
                .mapEntityToDto(admin);
        CarDto carDto = DtoMapperFactory.getInstance()
                .getCarDtoMapper()
                .mapEntityToDto(car);

        expectedReservation = new Reservation.Builder()
                .setId(id)
                .setClient(client)
                .setAdmin(admin)
                .setCar(car)
                .setState(state)
                .setPickUpDatetime(pickUpDateTime)
                .setDropOffDatetime(dropOffDatetime)
                .setRejectionReason(reason)
                .build();

        expectedReservationDto = new ReservationDto();
        expectedReservationDto.setId(id);
        expectedReservationDto.setClient(clientDto);
        expectedReservationDto.setAdmin(adminDto);
        expectedReservationDto.setCar(carDto);
        expectedReservationDto.setState(state);
        expectedReservationDto.setPickUpDatetime(pickUpDateTime);
        expectedReservationDto.setDropOffDatetime(dropOffDatetime);
        expectedReservationDto.setRejectionReason(reason);
        expectedReservationDto.setBillForServiceDto(null);
        expectedReservationDto.setBillForDamageDto(null);
    }

    @Test
    public void mapDtoToEntity() throws Exception {
        Reservation actualReservation = mapper.mapDtoToEntity(expectedReservationDto);

        assertThat(actualReservation, allOf(
                equalTo(expectedReservation),
                hasId(expectedReservation.getId())));
    }

    @Test
    public void mapEntityToDto() throws Exception {
        ReservationDto actualReservationDto = mapper.mapEntityToDto(expectedReservation);

        assertEquals(expectedReservationDto, actualReservationDto);
    }
}