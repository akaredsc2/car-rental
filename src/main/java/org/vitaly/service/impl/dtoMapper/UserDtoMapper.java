package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.notification.Notification;
import org.vitaly.model.reservation.Reservation;
import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.NotificationDto;
import org.vitaly.service.impl.dto.ReservationDto;
import org.vitaly.service.impl.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 23.04.17.
 */
public class UserDtoMapper implements DtoMapper<User, UserDto> {

    @Override
    public User mapDtoToEntity(UserDto dto) {

        // TODO: 23.04.17 consider removing reservations and notifications from user
        ReservationDtoMapper reservationDtoMapper = new ReservationDtoMapper();
        List<Reservation> reservationList = dto.getReservationDtoList()
                .stream()
                .map(reservationDtoMapper::mapDtoToEntity)
                .collect(Collectors.toList());

        NotificationDtoMapper notificationDtoMapper = new NotificationDtoMapper();
        List<Notification> notificationList = dto.getNotificationDtoList()
                .stream()
                .map(notificationDtoMapper::mapDtoToEntity)
                .collect(Collectors.toList());

        return new User.Builder()
                .setId(dto.getId())
                .setLogin(dto.getLogin())
                .setPassword(dto.getPassword())
                .setFullName(dto.getFullName())
                .setBirthDate(dto.getBirthDate())
                .setPassportNumber(dto.getPassportNumber())
                .setDriverLicenceNumber(dto.getDriverLicenceNumber())
                .setRole(dto.getRole())
                .setReservations(reservationList)
                .setNotifications(notificationList)
                .build();
    }

    @Override
    public UserDto mapEntityToDto(User entity) {
        UserDto userDto = new UserDto();

        // TODO: 23.04.17 consider removing reservations and notifications from user
        ReservationDtoMapper reservationDtoMapper = new ReservationDtoMapper();
        List<ReservationDto> reservationDtoList = entity.getReservations()
                .stream()
                .map(reservationDtoMapper::mapEntityToDto)
                .collect(Collectors.toList());

        NotificationDtoMapper notificationDtoMapper = new NotificationDtoMapper();
        List<NotificationDto> notificationDtoList = entity.getNotifications()
                .stream()
                .map(notificationDtoMapper::mapEntityToDto)
                .collect(Collectors.toList());

        userDto.setId(entity.getId());
        userDto.setLogin(entity.getLogin());
        userDto.setPassword(entity.getPassword());
        userDto.setFullName(entity.getFullName());
        userDto.setBirthDate(entity.getBirthDate());
        userDto.setPassportNumber(entity.getPassportNumber());
        userDto.setDriverLicenceNumber(entity.getDriverLicenceNumber());
        userDto.setRole(entity.getRole());
        userDto.setReservationDtoList(reservationDtoList);
        userDto.setNotificationDtoList(notificationDtoList);

        return userDto;
    }
}
