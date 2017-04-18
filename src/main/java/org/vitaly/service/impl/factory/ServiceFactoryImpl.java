package org.vitaly.service.impl.factory;

import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.service.abstraction.*;
import org.vitaly.service.abstraction.factory.ServiceFactory;
import org.vitaly.service.impl.*;

/**
 * Created by vitaly on 18.04.17.
 */
public class ServiceFactoryImpl implements ServiceFactory {
    private TransactionFactory transactionFactory;

    public ServiceFactoryImpl(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public BillService createBillService() {
        return new BillServiceImpl(transactionFactory);
    }

    @Override
    public CarService createCarService() {
        return new CarServiceImpl(transactionFactory);
    }

    @Override
    public LocationService createLocationService() {
        return new LocationServiceImpl(transactionFactory);
    }

    @Override
    public NotificationService createNotificationService() {
        return new NotificationServiceImpl(transactionFactory);
    }

    @Override
    public ReservationService createReservationService() {
        return new ReservationServiceImpl(transactionFactory);
    }

    @Override
    public UserService createUserService() {
        return new UserServiceImpl(transactionFactory);
    }
}
