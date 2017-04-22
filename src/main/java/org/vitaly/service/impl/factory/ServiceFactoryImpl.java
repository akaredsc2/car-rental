package org.vitaly.service.impl.factory;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.*;
import org.vitaly.service.abstraction.factory.ServiceFactory;
import org.vitaly.service.impl.*;

/**
 * Created by vitaly on 18.04.17.
 */
public class ServiceFactoryImpl implements ServiceFactory {
    private BillService billService;
    private CarModelService carModelService;
    private CarService carService;
    private LocationService locationService;
    private NotificationService notificationService;
    private ReservationService reservationService;
    private UserService userService;

    public ServiceFactoryImpl(TransactionFactory transactionFactory) {
        this.billService = new BillServiceImpl(transactionFactory);
        this.carModelService = new CarModelServiceImpl(transactionFactory);
        this.carService = new CarServiceImpl(transactionFactory);
        this.locationService = new LocationServiceImpl(transactionFactory);
        this.notificationService = new NotificationServiceImpl(transactionFactory);
        this.reservationService = new ReservationServiceImpl(transactionFactory);
        this.userService = new UserServiceImpl(transactionFactory);
    }

    @Override
    public BillService getBillService() {
        return billService;
    }

    @Override
    public CarModelService getCarModelService() {
        return carModelService;
    }

    @Override
    public CarService getCarService() {
        return carService;
    }

    @Override
    public LocationService getLocationService() {
        return locationService;
    }

    @Override
    public NotificationService getNotificationService() {
        return notificationService;
    }

    @Override
    public ReservationService getReservationService() {
        return reservationService;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
