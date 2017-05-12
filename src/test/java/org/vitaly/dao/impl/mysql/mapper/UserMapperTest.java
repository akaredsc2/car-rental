package org.vitaly.dao.impl.mysql.mapper;

import org.junit.Test;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.vitaly.matcher.EntityIdMatcher.hasId;
import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-15.
 */
public class UserMapperTest {
    private ResultSet resultSet = mock(ResultSet.class);
    private Mapper<User> mapper = new UserMapper();

    @Test
    public void mapCorrectlySetsUserParameters() throws Exception {
        User expectedUser = new User.Builder()
                .setId(7L)
                .setLogin("evilVitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("666sat666")
                .setDriverLicenceNumber("1313an1313")
                .setRole(UserRole.CLIENT)
                .build();

        when(resultSet.getLong(USERS_USER_ID)).thenReturn(expectedUser.getId());
        when(resultSet.getString(USERS_LOGIN)).thenReturn(expectedUser.getLogin());
        when(resultSet.getString(USERS_PASS)).thenReturn(expectedUser.getPassword());
        when(resultSet.getString(USERS_FULL_NAME)).thenReturn(expectedUser.getFullName());
        when(resultSet.getDate(USERS_BIRTH_DATE)).thenReturn(Date.valueOf(expectedUser.getBirthDate()));
        when(resultSet.getString(USERS_PASSPORT_NUMBER)).thenReturn(expectedUser.getPassportNumber());
        when(resultSet.getString(USERS_DRIVER_LICENCE_NUMBER)).thenReturn(expectedUser.getDriverLicenceNumber());
        when(resultSet.getString(USERS_ROLE)).thenReturn(expectedUser.getRole().toString());

        User actualUser = mapper.map(resultSet);

        assertThat(actualUser, allOf(
                equalTo(expectedUser),
                hasId(expectedUser.getId())));
    }
}