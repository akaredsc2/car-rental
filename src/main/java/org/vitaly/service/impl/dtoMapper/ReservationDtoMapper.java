package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;

/**
 * Created by vitaly on 23.04.17.
 */
public class ReservationDtoMapper implements DtoMapper<Reservation, ReservationDto> {

    @Override
    public Reservation mapDtoToEntity(ReservationDto dto) {
        UserDto clientDto = dto.getClient();

        // TODO: 23.04.17 consider using other dto mappers
        User dummyClient = User.createDummyClientWithId(clientDto.getId());

        // TODO: 23.04.17 refactor
        UserDto adminDto = dto.getAdmin();
        User dummyAdmin = null;
        if (adminDto != null) {
            dummyAdmin = User.createDummyAdminWithId(adminDto.getId());
        }

        CarDto carDto = dto.getCar();
        Car dummyCar = Car.createDummyCarWithId(carDto.getId());

        return new Reservation.Builder()
                .setId(dto.getId())
                .setClient(dummyClient)
                .setAdmin(dummyAdmin)
                .setCar(dummyCar)
                .setState(dto.getState())
                .setPickUpDatetime(dto.getPickUpDatetime())
                .setDropOffDatetime(dto.getDropOffDatetime())
                .setRejectionReason(dto.getRejectionReason())
                .build();
    }

    @Override
    public ReservationDto mapEntityToDto(Reservation entity) {
        ReservationDto reservationDto = new ReservationDto();

        // TODO: 23.04.17 consider using user, car and bill dto mappers
        UserDto clientDto = new UserDto();
        clientDto.setId(entity.getClient().getId());

        UserDto adminDto = null;
        User admin = entity.getAdmin();
        if (admin != null) {
            adminDto = new UserDto();
            adminDto.setId(admin.getId());
        }

        CarDto carDto = new CarDto();
        carDto.setId(entity.getCar().getId());

        reservationDto.setId(entity.getId());
        reservationDto.setClient(clientDto);
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        reservationDto.setState(entity.getState());
        reservationDto.setPickUpDatetime(entity.getPickUpDatetime());
        reservationDto.setDropOffDatetime(entity.getDropOffDatetime());
        reservationDto.setRejectionReason(entity.getRejectionReason());

        return reservationDto;
    }
}