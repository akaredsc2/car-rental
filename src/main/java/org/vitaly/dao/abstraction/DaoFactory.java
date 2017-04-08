package org.vitaly.dao.abstraction;

import org.vitaly.connectionPool.abstraction.PooledConnection;
import org.vitaly.dao.implementation.MysqlDaoFactory;

/**
 * Created by vitaly on 2017-03-26.
 */
public interface DaoFactory {
    LocationDao createLocationDao(PooledConnection connection);

    CarDao createCarDao(PooledConnection connection);

    UserDao createUserDao(PooledConnection connection);

    NotificationDao createNotificationDao(PooledConnection connection);

    ReservationDao createReservationDao(PooledConnection connection);

    BillDao createBillDao(PooledConnection connection);

    static DaoFactory getMysqlDaoFactory() {
        return MysqlDaoFactory.getInstance();
    }
}
