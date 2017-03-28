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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by vitaly on 2017-03-28.
 */
public class MysqlUserDaoTest {
    private static final String CLEAN_UP_QUERY = "delete from users";
    private static MysqlConnectionPool pool;
    private static DaoFactory factory;

    private User client;
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
        client = new User.Builder()
                .setLogin("vitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("ab123456")
                .setDriverLicenceNumber("123sdf456")
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
        User user = createUserWithId(client);

        assertThat(user, equalTo(client));
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
    public void findIdOfEntity() throws Exception {

    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void successfulCreationReturnsId() throws Exception {
        boolean isCreated = userDao.create(client).isPresent();

        assertTrue(isCreated);
    }

    @Test
    public void createdUserAlwaysInitiallyHasClientRole() throws Exception {
        User user = createUserWithId(admin);

        assertThat(user.getRole(), equalTo(UserRole.CLIENT));
    }

    @Test
    public void failedCreationReturnsEmptyOptional() throws Exception {
        userDao.create(client);
        userDao.create(client);

        boolean isCreated = userDao.create(client).isPresent();

        assertFalse(isCreated);
    }

    @Test
    public void createdUserHasClientRole() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void authenticate() throws Exception {

    }

    @Test
    public void changeRole() throws Exception {

    }

    @Test
    public void changePassword() throws Exception {

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