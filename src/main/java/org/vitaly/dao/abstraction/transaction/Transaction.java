package org.vitaly.dao.abstraction.transaction;

import org.vitaly.dao.abstraction.*;

/**
 * Created by vitaly on 2017-04-17.
 */
public interface Transaction extends AutoCloseable {
    BillDao getBillDao();

    CarDao getCarDao();

    LocationDao getLocationDao();

    NotificationDao getNotificationDao();

    ReservationDao getReservationDao();

    UserDao getUserDao();

    void commit();

    void rollback();

    @Override
    void close();
}
