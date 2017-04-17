package org.vitaly.dao.impl.mysql;

import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.dao.abstraction.connectionPool.PooledConnection;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;
import org.vitaly.dao.impl.mysql.template.DaoTemplate;
import org.vitaly.dao.abstraction.mapper.Mapper;
import org.vitaly.dao.impl.mysql.mapper.UserMapper;

import java.sql.Date;
import java.util.*;

import static org.vitaly.util.ExceptionThrower.unsupported;
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

    private Mapper<User> mapper;
    private DaoTemplate daoTemplate;

    public MysqlUserDao(PooledConnection connection) {
        this.mapper = new UserMapper();
        this.daoTemplate = new DaoTemplate(connection);
    }

    @Override
    public Optional<User> findById(long id) {
        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, id);

        User user = daoTemplate.executeSelectOne(FIND_BY_ID_QUERY, mapper, parameterMap);

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<Long> findIdOfEntity(User user) {
        requireNotNull(user, USER_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, user.getLogin());

        Long foundId = daoTemplate
                .executeSelectOne(FIND_ID_OF_USER_QUERY, resultSet -> resultSet.getLong(1), parameterMap);

        return Optional.ofNullable(foundId);
    }

    @Override
    public List<User> getAll() {
        return daoTemplate.executeSelect(GET_ALL_QUERY, mapper, Collections.emptyMap());
    }

    @Override
    public Optional<Long> create(User user) {
        requireNotNull(user, USER_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, user.getLogin());
        parameterMap.put(2, user.getPassword());
        parameterMap.put(3, user.getFullName());
        parameterMap.put(4, Date.valueOf(user.getBirthDate()));
        parameterMap.put(5, user.getPassportNumber());
        parameterMap.put(6, user.getDriverLicenceNumber());

        Long createdId = daoTemplate.executeInsert(CREATE_QUERY, parameterMap);
        return Optional.ofNullable(createdId);
    }

    @Override
    public int update(long id, User entity) {
        unsupported();
        return 0;
    }

    @Override
    public Optional<User> authenticate(String login, String password) {
        requireNotNull(login, LOGIN_MUST_NOT_BE_NULL);
        requireNotNull(password, PASSWORD_MUST_NOT_BE_NULL);

        Map<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, login);
        parameterMap.put(2, password);

        User authenticatedUser = daoTemplate.executeSelectOne(AUTHENTICATE_QUERY, mapper, parameterMap);

        return Optional.ofNullable(authenticatedUser);
    }

    @Override
    public void changeRole(long userId, UserRole role) {
        requireNotNull(role, ROLE_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, role.toString().toLowerCase());
        parameterMap.put(2, userId);

        daoTemplate.executeUpdate(CHANGE_ROLE_QUERY, parameterMap);
    }

    @Override
    public void changePassword(long userId, String newPassword) {
        requireNotNull(newPassword, NEW_PASSWORD_MUST_NOT_BE_NULL);

        HashMap<Integer, Object> parameterMap = new HashMap<>();
        parameterMap.put(1, newPassword);
        parameterMap.put(2, userId);

        daoTemplate.executeUpdate(CHANGE_PASSWORD_QUERY, parameterMap);
    }
}
