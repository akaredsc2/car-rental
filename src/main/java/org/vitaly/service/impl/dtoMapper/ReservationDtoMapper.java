package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.BillDto;
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

        BillDtoMapper billDtoMapper = new BillDtoMapper();

        BillDto billForServiceDto = dto.getBillForServiceDto();
        Bill billForService = null;
        if (billForServiceDto != null) {
            billForService = billDtoMapper.mapDtoToEntity(billForServiceDto);
        }

        BillDto billForDamageDto = dto.getBillForDamageDto();
        Bill billForDamage = null;
        if (billForDamageDto != null) {
            billForDamage = billDtoMapper.mapDtoToEntity(billForDamageDto);
        }

        return new Reservation.Builder()
                .setId(dto.getId())
                .setClient(dummyClient)
                .setAdmin(dummyAdmin)
                .setCar(dummyCar)
                .setState(dto.getState())
                .setPickUpDatetime(dto.getPickUpDatetime())
                .setDropOffDatetime(dto.getDropOffDatetime())
                .setRejectionReason(dto.getRejectionReason())
                .setBillForService(billForService)
                .setBillForDamage(billForDamage)
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

        BillDto billForServiceDto = null;
        Bill billForService = entity.getBillForService();
        if (billForService != null) {
            billForServiceDto = new BillDto();
            billForServiceDto.setId(billForService.getId());
        }

        BillDto billForDamageDto = null;
        Bill billForDamage = entity.getBillForDamage();
        if (billForDamage != null) {
            billForDamageDto = new BillDto();
            billForDamageDto.setId(billForDamageDto.getId());
        }

        reservationDto.setId(entity.getId());
        reservationDto.setClient(clientDto);
        reservationDto.setAdmin(adminDto);
        reservationDto.setCar(carDto);
        reservationDto.setState(entity.getState());
        reservationDto.setPickUpDatetime(entity.getPickUpDatetime());
        reservationDto.setDropOffDatetime(entity.getDropOffDatetime());
        reservationDto.setRejectionReason(entity.getRejectionReason());
        reservationDto.setBillForServiceDto(billForServiceDto);
        reservationDto.setBillForDamageDto(billForDamageDto);

        return reservationDto;
    }
}