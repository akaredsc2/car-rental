package org.vitaly.service.impl.dtoMapper;

import org.junit.Before;
import org.junit.Test;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.service.impl.dto.UserDto;
import org.vitaly.service.impl.factory.DtoMapperFactory;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 25.04.17.
 */
public class UserDtoMapperTest {
    private DtoMapper<User, UserDto> mapper = DtoMapperFactory.getInstance().getUserDtoMapper();
    private UserDto expectedUserDto;
    private User expectedUser;

    @Before
    public void setUp() throws Exception {
        long id = 67;
        String login = "login";
        String password = "password";
        String fullName = "fool name";
        String passportNumber = "passport";
        String DriverLicenceNumber = "licence";
        LocalDate birthDate = LocalDate.now();
        UserRole role = UserRole.CLIENT;

        expectedUserDto = new UserDto();
        expectedUserDto.setId(id);
        expectedUserDto.setLogin(login);
        expectedUserDto.setPassword(password);
        expectedUserDto.setFullName(fullName);
        expectedUserDto.setPassportNumber(passportNumber);
        expectedUserDto.setDriverLicenceNumber(DriverLicenceNumber);
        expectedUserDto.setBirthDate(birthDate);
        expectedUserDto.setRole(role);

        expectedUser = new User.Builder()
                .setId(id)
                .setLogin(login)
                .setPassword(password)
                .setFullName(fullName)
                .setPassportNumber(passportNumber)
                .setDriverLicenceNumber(DriverLicenceNumber)
                .setBirthDate(birthDate)
                .setRole(role)
                .build();
    }

    @Test
    public void mapDtoToEntity() throws Exception {
        User actualUser = mapper.mapDtoToEntity(expectedUserDto);

        assertThat(actualUser, allOf(
                equalTo(expectedUser),
                hasId(expectedUser.getId())));
    }

    @Test
    public void mapEntityToDto() throws Exception {
        UserDto actualUserDto = mapper.mapEntityToDto(expectedUser);

        assertEquals(expectedUserDto, actualUserDto);
    }
}