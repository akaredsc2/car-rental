package org.vitaly.service.impl.factory;

import org.vitaly.service.abstraction.*;
import org.vitaly.service.impl.*;

/**
 * Created by vitaly on 18.04.17.
 */
public class ServiceFactory {
    private static ServiceFactory instance = new ServiceFactory();

    private BillService billService = new BillServiceImpl();
    private CarModelService carModelService = new CarModelServiceImpl();
    private CarService carService = new CarServiceImpl();
    private LocationService locationService = new LocationServiceImpl();
    private NotificationService notificationService = new NotificationServiceImpl();
    private ReservationService reservationService = new ReservationServiceImpl();
    private UserService userService = new UserServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public BillService getBillService() {
        return billService;
    }

    public CarModelService getCarModelService() {
        return carModelService;
    }

    public CarService getCarService() {
        return carService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public UserService getUserService() {
        return userService;
    }
}
