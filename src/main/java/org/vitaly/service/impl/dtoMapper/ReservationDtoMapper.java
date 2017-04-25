package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.CarDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.DtoMapperFactory;

/**
 * Created by vitaly on 23.04.17.
 */
public class ReservationDtoMapper implements DtoMapper<Reservation, ReservationDto> {

    @Override
    public Reservation mapDtoToEntity(ReservationDto dto) {
        UserDto clientDto = dto.getClient();
        User dummyClient = DtoMapperFactory.getInstance()
                .getUserDtoMapper()
                .mapDtoToEntity(clientDto);

        UserDto adminDto = dto.getAdmin();
        User dummyAdmin = null;
        if (adminDto != null) {
            dummyAdmin = DtoMapperFactory.getInstance()
                    .getUserDtoMapper()
                    .mapDtoToEntity(adminDto);
        }

        CarDto carDto = dto.getCar();
        Car dummyCar = DtoMapperFactory.getInstance()
                .getCarDtoMapper()
                .mapDtoToEntity(carDto);

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
        User client = entity.getClient();
        UserDto clientDto = DtoMapperFactory.getInstance().getUserDtoMapper().mapEntityToDto(client);

        UserDto adminDto = null;
        User admin = entity.getAdmin();
        if (admin != null) {
            adminDto = DtoMapperFactory.getInstance()
                    .getUserDtoMapper()
                    .mapEntityToDto(admin);
        }

        Car car = entity.getCar();
        CarDto carDto = DtoMapperFactory.getInstance()
                .getCarDtoMapper()
                .mapEntityToDto(car);

        ReservationDto reservationDto = new ReservationDto();
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