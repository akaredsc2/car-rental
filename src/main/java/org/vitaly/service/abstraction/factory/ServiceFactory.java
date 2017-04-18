package org.vitaly.service.abstraction.factory;

import org.vitaly.service.abstraction.*;

/**
 * Created by vitaly on 18.04.17.
 */
public interface ServiceFactory {
    BillService createBillService();

    CarService createCarService();

    LocationService createLocationService();

    NotificationService createNotificationService();

    ReservationService createReservationService();

    UserService createUserService();
}
