package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-28.
 */
public class MysqlUserDao implements UserDao {
    private static final String USERS_USER_ID = "users.user_id";
    private static final String USERS_LOGIN = "users.login";
    private static final String USERS_PASS = "users.pass";
    private static final String USERS_FULL_NAME = "users.full_name";
    private static final String USERS_BIRTH_DATE = "users.birth_date";
    private static final String USERS_PASSPORT_NUMBER = "users.passport_number";
    private static final String USERS_DRIVER_LICENCE_NUMBER = "users.driver_licence_number";
    private static final String USERS_ROLE = "users.role";

    private static final String FIND_BY_ID_QUERY =
            "SELECT * " +
            "FROM users " +
            "WHERE user_id = ?";
    private static final String FIND_ID_OF_USER_QUERY =
            "SELECT users.user_id " +
            "FROM users " +
            "WHERE users.login = ?";
    private static final String GET_ALL_QUERY =
            "SELECT * " +
            "FROM users";
    private static final String CREATE_QUERY =
            "INSERT INTO users(login, pass, full_name, birth_date, passport_number, driver_licence_number) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String AUTHENTICATE_QUERY =
            "SELECT * " +
            "FROM users " +
            "WHERE users.login = ? and users.pass = ?";
    private static final String CHANGE_ROLE_QUERY =
            "UPDATE users " +
            "SET users.role = ? " +
            "WHERE users.user_id = ?";
    private static final String CHANGE_PASSWORD_QUERY =
            "UPDATE users " +
            "SET users.pass = ? " +
            "WHERE users.user_id = ?";

    private static final String ID_MUST_NOT_BE_NULL = "Id must not be null!";
    private static final String LOGIN_MUST_NOT_BE_NULL = "Login must not be null!";
    private static final String PASSWORD_MUST_NOT_BE_NULL = "Password must not be null!";
    private static final String ROLE_MUST_NOT_BE_NULL = "Role must not be null!";
    private static final String USER_MUST_NOT_BE_NULL = "User must not be null!";
    private static final String NEW_PASSWORD_MUST_NOT_BE_NULL = "New password must not be null!";

    private static final Logger logger = LogManager.getLogger(MysqlUserDao.class.getName());

    private PooledConnection connection;

    MysqlUserDao(PooledConnection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<User> findById(long id) {
        requireNotNull(id, ID_MUST_NOT_BE_NULL);

        User foundUser = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                foundUser = buildUserFromResultSetEntry(resultSet);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while finding user by id.", e);
        }

        return Optional.ofNullable(foundUser);
    }

    private User buildUserFromResultSetEntry(ResultSet resultSet) throws SQLException {
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

    @Override
    public OptionalLong findIdOfEntity(User user) {
        requireNotNull(user, USER_MUST_NOT_BE_NULL);

        OptionalLong findResult = OptionalLong.empty();

        try (PreparedStatement statement = connection.prepareStatement(FIND_ID_OF_USER_QUERY)) {
            statement.setString(1, user.getLogin());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                findResult = OptionalLong.of(resultSet.getLong(USERS_USER_ID));
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while finding id of user.", e);
        }

        return findResult;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY)) {
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                User nextUser = buildUserFromResultSetEntry(resultSet);
                users.add(nextUser);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while getting all users.", e);
        }
        return users;
    }

    @Override
    public OptionalLong create(User user) {
        requireNotNull(user, USER_MUST_NOT_BE_NULL);

        connection.initializeTransaction();

        OptionalLong createdId = OptionalLong.empty();
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, RETURN_GENERATED_KEYS)) {
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
            logger.error("Error while creating user. Rolling back transaction.", e);
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
    public int update(long id, User entity) {
        RuntimeException e = new UnsupportedOperationException();
        logger.error("Error while calling unsupported operation.", e);
        throw e;
    }

    @Override
    public Optional<User> authenticate(String login, String password) {
        requireNotNull(login, LOGIN_MUST_NOT_BE_NULL);
        requireNotNull(password, PASSWORD_MUST_NOT_BE_NULL);

        User authenticatedUser = null;

        try (PreparedStatement statement = connection.prepareStatement(AUTHENTICATE_QUERY)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                authenticatedUser = buildUserFromResultSetEntry(resultSet);
            }

            resultSet.close();
        } catch (SQLException e) {
            logger.error("Error while authenticating user.", e);
        }

        return Optional.ofNullable(authenticatedUser);
    }

    @Override
    public void changeRole(User user, UserRole role) {
        requireNotNull(user, USER_MUST_NOT_BE_NULL);
        requireNotNull(role, ROLE_MUST_NOT_BE_NULL);

        if (user.getRole() != role) {
            connection.initializeTransaction();

            try (PreparedStatement statement = connection.prepareStatement(CHANGE_ROLE_QUERY)) {
                statement.setString(1, role.toString().toLowerCase());
                statement.setLong(2, user.getId());
                statement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Error while changing user role. Rolling back transaction.", e);
            }
        }
    }

    @Override
    public void changePassword(User user, String newPassword) {
        requireNotNull(user, USER_MUST_NOT_BE_NULL);
        requireNotNull(newPassword, NEW_PASSWORD_MUST_NOT_BE_NULL);

        if (!Objects.equals(user.getPassword(), newPassword)) {
            connection.initializeTransaction();

            try (PreparedStatement statement = connection.prepareStatement(CHANGE_PASSWORD_QUERY)) {
                statement.setString(1, newPassword);
                statement.setLong(2, user.getId());
                statement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Error while changing user password. Rolling back transaction.", e);
            }
        }
    }
}
