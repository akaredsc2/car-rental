package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-28.
 */
public class MysqlUserDaoTest {
    private static final String CLEAN_UP_QUERY = "delete from users";
    private static MysqlConnectionPool pool;
    private static DaoFactory factory;

    private User client1;
    private User client2;
    private User admin;
    private PooledConnection connection;
    private UserDao userDao;

    @BeforeClass
    public static void initPoolAndFactory() {
        pool = MysqlConnectionPool.getTestInstance();
        factory = DaoFactory.getMysqlDaoFactory();
    }

    @Before
    public void setUp() throws Exception {
        client1 = new User.Builder()
                .setLogin("vitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("ab123456")
                .setDriverLicenceNumber("123sdf456")
                .setRole(UserRole.CLIENT)
                .build();

        client2 = new User.Builder()
                .setLogin("evilVitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("666sat666")
                .setDriverLicenceNumber("1313an1313")
                .setRole(UserRole.CLIENT)
                .build();

        admin = new User.Builder()
                .setLogin("Karsa")
                .setPassword("toblakai")
                .setFullName("Karsa Orlong from Uryd Tribe")
                .setBirthDate(LocalDate.of(1937, Month.JANUARY, 1))
                .setPassportNumber("cd789101")
                .setDriverLicenceNumber("789def101")
                .setRole(UserRole.ADMIN)
                .build();

        connection = pool.getConnection();
        userDao = factory.createUserDao(connection);
    }

    @Test
    public void findByIdExistingUserReturnsUser() throws Exception {
        User user = createUserWithId(client1);

        assertThat(user, equalTo(client1));
    }

    private User createUserWithId(User user) {
        long createUserId = userDao.create(user).orElseThrow(AssertionError::new);
        return userDao.findById(createUserId).orElseThrow(AssertionError::new);
    }

    @Test
    public void findByIdNonExistingUserReturnsEmptyOptional() throws Exception {
        boolean findResult = userDao.findById(-1L).isPresent();

        assertFalse(findResult);
    }

    @Test
    public void findIdOfExistingEntityReturnsId() throws Exception {
        userDao.create(client1);
        long foundId = userDao.findIdOfEntity(client1).orElseThrow(AssertionError::new);

        User foundUser = userDao.findById(foundId).orElseThrow(AssertionError::new);
        assertThat(foundUser, equalTo(client1));
    }

    @Test
    public void findIdOfNonExistingEntityReturnsEmptyOptional() throws Exception {
        boolean findResult = userDao.findIdOfEntity(admin).isPresent();

        assertFalse(findResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIdOfNullShouldThrowException() throws Exception {
        userDao.findIdOfEntity(null);
    }

    @Test
    public void getAllOnEmptyUsersReturnsEmptyList() throws Exception {
        List<User> userList = userDao.getAll();

        assertThat(userList, empty());
    }

    @Test
    public void getAllReturnsAllExistingUsers() throws Exception {
        userDao.create(client1);
        userDao.create(client2);

        List<User> userList = userDao.getAll();

        assertThat(userList, hasItems(client1, client2));
    }

    @Test
    public void successfulCreationReturnsId() throws Exception {
        boolean isCreated = userDao.create(client1).isPresent();

        assertTrue(isCreated);
    }

    @Test
    public void createdUserAlwaysInitiallyHasClientRole() throws Exception {
        User exAdmin = createUserWithId(admin);

        assertThat(exAdmin.getRole(), equalTo(UserRole.CLIENT));
    }

    @Test
    public void failedCreationReturnsEmptyOptional() throws Exception {
        userDao.create(client1);
        userDao.create(client1);

        boolean isCreated = userDao.create(client1).isPresent();

        assertFalse(isCreated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullUserShouldThrowException() throws Exception {
        userDao.create(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateShouldThrowException() throws Exception {
        userDao.update(1L, client1);
    }

    @Test
    public void authenticateWithCorrectLoginAndPasswordReturnsUser() throws Exception {
        String login = client1.getLogin();
        String password = client1.getPassword();
        userDao.create(client1);

        User authenticatedUser = userDao.authenticate(login, password).orElseThrow(AssertionError::new);

        assertThat(authenticatedUser, equalTo(client1));
    }

    @Test
    public void authenticateWithCorrectLoginAndWrongPasswordReturnsEmptyOptional() throws Exception {
        String login = client1.getLogin();
        userDao.create(client1);

        boolean isAuthenticated = userDao.authenticate(login, login).isPresent();

        assertFalse(isAuthenticated);
    }

    @Test
    public void authenticateWithWrongLoginAndCorrectPasswordReturnsEmptyOptional() throws Exception {
        String password = client1.getPassword();
        userDao.create(client1);

        boolean isAuthenticated = userDao.authenticate(password, password).isPresent();

        assertFalse(isAuthenticated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void authenticateWithNullLoginShouldThrowException() throws Exception {
        userDao.authenticate(null, client1.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void authenticateWithNullPasswordShouldThrowException() throws Exception {
        userDao.authenticate(client1.getLogin(), null);
    }

    @Test
    public void changeRoleToSameRoleDoesNotChangeRole() throws Exception {
        User client = createUserWithId(client1);
        UserRole roleBeforeChange = client.getRole();

        UserRole newRole = UserRole.CLIENT;
        userDao.changeRole(client, newRole);

        User updatedClient = userDao.findById(client.getId()).orElseThrow(AssertionError::new);
        UserRole changedRole = updatedClient.getRole();
        assertThat(changedRole, allOf(
                equalTo(newRole),
                equalTo(roleBeforeChange)));
    }

    @Test
    public void changeRoleToAnotherRoleDoesChangeRole() throws Exception {
        User futureAdmin = createUserWithId(admin);
        UserRole roleBeforeChange = futureAdmin.getRole();

        UserRole newRole = UserRole.ADMIN;
        userDao.changeRole(futureAdmin, newRole);

        User updatedAdmin = userDao.findById(futureAdmin.getId()).orElseThrow(AssertionError::new);
        UserRole changedRole = updatedAdmin.getRole();
        assertThat(changedRole, allOf(
                equalTo(newRole),
                not(equalTo(roleBeforeChange))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeRoleOfNullUserShouldThrowException() throws Exception {
        userDao.changeRole(null, UserRole.ADMIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeRoleToNullShouldThrowException() throws Exception {
        userDao.changeRole(client1, null);
    }

    @Test
    public void changePasswordToSamePasswordDoesNotChangePassword() throws Exception {
        User client = createUserWithId(client1);
        String passwordBeforeChange = client.getPassword();

        userDao.changePassword(client, passwordBeforeChange);

        User updatedClient = userDao.findById(client.getId()).orElseThrow(AssertionError::new);
        String changedPassword = updatedClient.getPassword();
        assertThat(changedPassword, equalTo(passwordBeforeChange));
    }

    @Test
    public void changePasswordToAnotherPasswordDoesChangePassword() throws Exception {
        User client = createUserWithId(client1);
        String passwordBeforeChange = client.getPassword();

        String newPassword = passwordBeforeChange + "password";
        userDao.changePassword(client, newPassword);

        User updatedClient = userDao.findById(client.getId()).orElseThrow(AssertionError::new);
        String changedPassword = updatedClient.getPassword();
        assertThat(changedPassword, allOf(
                equalTo(newPassword),
                not(equalTo(passwordBeforeChange))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void changePasswordOfNullUserShouldThrowException() throws Exception {
        userDao.changePassword(null, client1.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void changePasswordToNullShouldThrowException() throws Exception {
        userDao.changePassword(client1, null);
    }

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
        connection.close();
    }
}