package org.vitaly.dao.implementation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.connectionPool.implementation.MysqlConnectionPool;
import org.vitaly.dao.abstraction.DaoFactory;
import org.vitaly.dao.abstraction.UserDao;
import org.vitaly.data.TestData;
import org.vitaly.data.TestUtil;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.lang.reflect.Field;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.vitaly.matcher.EntityIdMatcher.hasId;

/**
 * Created by vitaly on 2017-03-28.
 */
public class MysqlUserDaoTest {
    private static final String CLEAN_UP_QUERY = "delete from users";

    private static PooledConnection connection = MysqlConnectionPool.getTestInstance().getConnection();
    private static UserDao userDao = DaoFactory.getMysqlDaoFactory().createUserDao(connection);

    private User client1 = TestData.getInstance().getUser("client1");
    private User client2 = TestData.getInstance().getUser("client2");
    private User admin = TestData.getInstance().getUser("admin");

    @After
    public void tearDown() throws Exception {
        connection.initializeTransaction();
        connection.prepareStatement(CLEAN_UP_QUERY)
                .executeUpdate();
        connection.commit();
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        connection.close();
    }

    @Test
    public void findByIdExistingUserReturnsUser() throws Exception {
        User user = TestUtil.createEntityWithId(client1, userDao);

        assertThat(user, equalTo(client1));
    }

    @Test
    public void findByIdNonExistingUserReturnsEmptyOptional() throws Exception {
        boolean findResult = userDao.findById(-1L).isPresent();

        assertFalse(findResult);
    }

    @Test
    public void findIdOfExistingEntityReturnsId() throws Exception {
        User createdUser = TestUtil.createEntityWithId(client1, userDao);

        long foundId = userDao.findIdOfEntity(client1).orElseThrow(AssertionError::new);

        assertThat(createdUser, hasId(foundId));
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
        User exAdmin = TestUtil.createEntityWithId(admin, userDao);

        assertThat(exAdmin.getRole(), equalTo(UserRole.CLIENT));
    }

    @Test
    public void creatingUserWithSameLoginReturnsEmptyOptional() throws Exception {
        userDao.create(client1);
        userDao.create(client1);

        boolean isCreated = userDao.create(client1).isPresent();

        assertFalse(isCreated);
    }

    @Test
    public void creatingUserWithSamePassportNumberReturnsEmptyOptional() throws Exception {
        userDao.create(client1);

        Field passportNumberField = client2.getClass().getDeclaredField("passportNumber");
        passportNumberField.setAccessible(true);
        passportNumberField.set(client2, client1.getPassportNumber());
        userDao.create(client2);

        boolean isCreated = userDao.create(client2).isPresent();

        assertFalse(isCreated);
    }

    @Test
    public void creatingUserWithSameDriverLicenceNumberReturnsEmptyOptional() throws Exception {
        userDao.create(client1);

        Field passportNumberField = client2.getClass().getDeclaredField("driverLicenceNumber");
        passportNumberField.setAccessible(true);
        passportNumberField.set(client2, client1.getDriverLicenceNumber());
        userDao.create(client2);

        boolean isCreated = userDao.create(client2).isPresent();

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
        User client = TestUtil.createEntityWithId(client1, userDao);
        UserRole roleBeforeChange = client.getRole();

        UserRole newRole = UserRole.CLIENT;
        userDao.changeRole(client.getId(), newRole);

        User updatedClient = userDao.findById(client.getId()).orElseThrow(AssertionError::new);
        UserRole changedRole = updatedClient.getRole();
        assertThat(changedRole, allOf(
                equalTo(newRole),
                equalTo(roleBeforeChange)));
    }

    @Test
    public void changeRoleToAnotherRoleDoesChangeRole() throws Exception {
        User futureAdmin = TestUtil.createEntityWithId(admin, userDao);
        UserRole roleBeforeChange = futureAdmin.getRole();

        UserRole newRole = UserRole.ADMIN;
        userDao.changeRole(futureAdmin.getId(), newRole);

        User updatedAdmin = userDao.findById(futureAdmin.getId()).orElseThrow(AssertionError::new);
        UserRole changedRole = updatedAdmin.getRole();
        assertThat(changedRole, allOf(
                equalTo(newRole),
                not(equalTo(roleBeforeChange))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeRoleToNullShouldThrowException() throws Exception {
        userDao.changeRole(client1.getId(), null);
    }

    @Test
    public void changePasswordToSamePasswordDoesNotChangePassword() throws Exception {
        User client = TestUtil.createEntityWithId(client1, userDao);
        String passwordBeforeChange = client.getPassword();

        userDao.changePassword(client.getId(), passwordBeforeChange);

        User updatedClient = userDao.findById(client.getId()).orElseThrow(AssertionError::new);
        String changedPassword = updatedClient.getPassword();
        assertThat(changedPassword, equalTo(passwordBeforeChange));
    }

    @Test
    public void changePasswordToAnotherPasswordDoesChangePassword() throws Exception {
        User client = TestUtil.createEntityWithId(client1, userDao);
        String passwordBeforeChange = client.getPassword();

        String newPassword = passwordBeforeChange + "password";
        userDao.changePassword(client.getId(), newPassword);

        User updatedClient = userDao.findById(client.getId()).orElseThrow(AssertionError::new);
        String changedPassword = updatedClient.getPassword();
        assertThat(changedPassword, allOf(
                equalTo(newPassword),
                not(equalTo(passwordBeforeChange))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void changePasswordToNullShouldThrowException() throws Exception {
        userDao.changePassword(client1.getId(), null);
    }
}