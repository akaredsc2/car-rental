package org.vitaly.service.impl.factory;

import org.vitaly.service.abstraction.*;
import org.vitaly.service.impl.*;

/**
 * Created by vitaly on 18.04.17.
 */
public class ServiceFactory {
    private static ServiceFactory instance = new ServiceFactory();

    private BillService billService;
    private CarModelService carModelService;
    private CarService carService;
    private LocationService locationService;
    private NotificationService notificationService;
    private ReservationService reservationService;
    private UserService userService;

    private ServiceFactory() {
        this.billService = new BillServiceImpl();
        this.carModelService = new CarModelServiceImpl();
        this.carService = new CarServiceImpl();
        this.locationService = new LocationServiceImpl();
        this.notificationService = new NotificationServiceImpl();
        this.reservationService = new ReservationServiceImpl();
        this.userService = new UserServiceImpl();
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
