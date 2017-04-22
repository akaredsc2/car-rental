package org.vitaly.service.impl.facade;

import org.vitaly.dao.abstraction.facade.DaoFacade;
import org.vitaly.dao.impl.mysql.facade.MysqlDaoFacade;
import org.vitaly.service.abstraction.facade.ServiceFacade;
import org.vitaly.service.abstraction.factory.ServiceFactory;
import org.vitaly.service.impl.factory.ServiceFactoryImpl;

/**
 * Created by vitaly on 18.04.17.
 */
public class ServiceFacadeImpl implements ServiceFacade {
    private static ServiceFacadeImpl instance = new ServiceFacadeImpl();

    private ServiceFactory serviceFactory;

    private ServiceFacadeImpl() {
        init();
    }

    public static ServiceFacadeImpl getInstance() {
        return instance;
    }

    @Override
    public void init() {
        DaoFacade daoFacade = MysqlDaoFacade.getInstance();
        this.serviceFactory = new ServiceFactoryImpl(daoFacade.getTransactionFactory());
    }

    @Override
    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }
}
