package org.vitaly.service.impl.dtoMapper;

import org.vitaly.model.user.User;
import org.vitaly.service.impl.dto.UserDto;

import java.util.Collections;

/**
 * Created by vitaly on 23.04.17.
 */
public class UserDtoMapper implements DtoMapper<User, UserDto> {

    @Override
    public User mapDtoToEntity(UserDto dto) {
        return new User.Builder()
                .setId(dto.getId())
                .setLogin(dto.getLogin())
                .setPassword(dto.getPassword())
                .setFullName(dto.getFullName())
                .setBirthDate(dto.getBirthDate())
                .setPassportNumber(dto.getPassportNumber())
                .setDriverLicenceNumber(dto.getDriverLicenceNumber())
                .setRole(dto.getRole())
                .build();
    }

    @Override
    public UserDto mapEntityToDto(User entity) {
        UserDto userDto = new UserDto();

        userDto.setId(entity.getId());
        userDto.setLogin(entity.getLogin());
        userDto.setPassword(entity.getPassword());
        userDto.setFullName(entity.getFullName());
        userDto.setBirthDate(entity.getBirthDate());
        userDto.setPassportNumber(entity.getPassportNumber());
        userDto.setDriverLicenceNumber(entity.getDriverLicenceNumber());
        userDto.setRole(entity.getRole());
        userDto.setReservationDtoList(Collections.emptyList());

        return userDto;
    }
}
