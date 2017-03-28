package org.vitaly.dao.implementation;

import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-28.
 */
public class MysqlUserDao implements UserDao {
    private PooledConnection connection;

    MysqlUserDao(PooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<User> findById(long id) {
        requireNotNull(id, "Id must not be null!");

        User foundUser = null;

        try (PreparedStatement statement = connection.prepareStatement("select * from users where user_id = ?")) {
            statement.setLong(1, id);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                foundUser = buildUserFromResultSetEntry(resultSet);
            }

            resultSet.close();
        } catch (SQLException e) {

            // TODO: 2017-03-28 log
            e.printStackTrace();
        }

        return Optional.ofNullable(foundUser);
    }

    private User buildUserFromResultSetEntry(ResultSet resultSet) throws SQLException {
        LocalDate birthDate = resultSet.getDate("users.birth_date").toLocalDate();
        UserRole role = UserRole.valueOf(resultSet.getString("users.role").toUpperCase());

        return new User.Builder()
                .setId(resultSet.getLong("users.user_id"))
                .setLogin(resultSet.getString("users.login"))
                .setPassword(resultSet.getString("users.pass"))
                .setFullName(resultSet.getString("users.full_name"))
                .setBirthDate(birthDate)
                .setPassportNumber(resultSet.getString("users.passport_number"))
                .setDriverLicenceNumber(resultSet.getString("users.driver_licence_number"))
                .setRole(role)
                .build();
    }

    @Override
    public OptionalLong findIdOfEntity(User entity) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public OptionalLong create(User user) {
        requireNotNull(user, "User must not be null!");

        connection.initializeTransaction();

        OptionalLong createdId = OptionalLong.empty();
        try (PreparedStatement statement = connection.prepareStatement(
                "insert into users(login, pass, full_name, birth_date, passport_number, driver_licence_number) " +
                        "values (?, ?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
            setUserParametersInStatement(user, statement);

            // TODO: 2017-03-28 transaction isolation
            statement.executeUpdate();

            connection.commit();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                createdId = OptionalLong.of(resultSet.getLong(1));
            }

            resultSet.close();
        } catch (SQLException e) {
            connection.rollback();

            // TODO: 2017-03-28 log
            e.printStackTrace();
        }

        return createdId;
    }

    private void setUserParametersInStatement(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFullName());

        Date date = Date.valueOf(user.getBirthDate());
        statement.setDate(4, date);
        statement.setString(5, user.getPassportNumber());
        statement.setString(6, user.getDriverLicenceNumber());
    }

    @Override
    public int update(Long id, User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> authenticate(String login, String password) {
        return null;
    }

    @Override
    public boolean changeRole(User user, UserRole role) {
        return false;
    }

    @Override
    public boolean changePassword(User user, String newPassword) {
        return false;
    }
}
