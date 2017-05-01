package org.vitaly.dao.impl.mysql.mapper;

import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.vitaly.util.constants.TableAttributes.*;

/**
 * Created by vitaly on 2017-04-07.
 */
public class UserMapper implements Mapper<User> {

    @Override
    public User map(ResultSet resultSet) throws SQLException {
        LocalDate birthDate = resultSet.getDate(USERS_BIRTH_DATE).toLocalDate();
        UserRole role = UserRole.valueOf(resultSet.getString(USERS_ROLE).toUpperCase());

        return new User.Builder()
                .setId(resultSet.getLong(USERS_USER_ID))
                .setLogin(resultSet.getString(USERS_LOGIN))
                .setPassword(resultSet.getString(USERS_PASS))
                .setFullName(resultSet.getString(USERS_FULL_NAME))
                .setBirthDate(birthDate)
                .setPassportNumber(resultSet.getString(USERS_PASSPORT_NUMBER))
                .setDriverLicenceNumber(resultSet.getString(USERS_DRIVER_LICENCE_NUMBER))
                .setRole(role)
                .build();
    }
}
