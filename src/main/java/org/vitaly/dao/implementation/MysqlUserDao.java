package org.vitaly.dao.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.util.dao.DaoTemplate;
import org.vitaly.util.dao.mapper.Mapper;
import org.vitaly.util.dao.mapper.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-28.
 */
public class MysqlUserDao implements UserDao {
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

    private static final String LOGIN_MUST_NOT_BE_NULL = "Login must not be null!";
    private static final String PASSWORD_MUST_NOT_BE_NULL = "Password must not be null!";
    private static final String ROLE_MUST_NOT_BE_NULL = "Role must not be null!";
    private static final String USER_MUST_NOT_BE_NULL = "User must not be null!";
    private static final String NEW_PASSWORD_MUST_NOT_BE_NULL = "New password must not be null!";

    private static final Logger logger = LogManager.getLogger(MysqlUserDao.class.getName());

    private PooledConnection connection;
    private Mapper<User> mapper;
    private DaoTemplate daoTemplate;

    MysqlUserDao(PooledConnection connection) {
        this.connection = connection;
        this.mapper = new UserMapper();
        this.daoTemplate = new DaoTemplate(connection);
    }

    @Override
    public Optional<User> findById(long id) {
        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, id);

        User user = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);

        return Optional.ofNullable(user);
    }

    @Override
    public OptionalLong findIdOfEntity(User user) {
        requireNotNull(user, USER_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, user.getLogin());

        Long foundId = daoTemplate
                .executeSelectOne(FIND_ID_OF_USER_QUERY, resultSet -> resultSet.getLong(1), parameterMap);

        return foundId == null ? OptionalLong.empty() : OptionalLong.of(foundId);
    }

    @Override
    public List<User> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, new TreeMap<>());
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

        Map<Integer, Object> parameterMap = new TreeMap<>();
        parameterMap.put(1, login);
        parameterMap.put(2, password);

        User authenticatedUser = daoTemplate.executeSelectOne(AUTHENTICATE_QUERY, mapper, parameterMap);

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
