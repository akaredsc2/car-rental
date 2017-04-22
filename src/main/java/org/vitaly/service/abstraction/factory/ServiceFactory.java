package org.vitaly.service.abstraction.factory;

import org.vitaly.service.abstraction.*;

/**
 * Created by vitaly on 18.04.17.
 */
public interface ServiceFactory {
    BillService getBillService();

    CarModelService getCarModelService();

    CarService getCarService();

    LocationService getLocationService();

    NotificationService getNotificationService();

    ReservationService getReservationService();

    UserService getUserService();
}
