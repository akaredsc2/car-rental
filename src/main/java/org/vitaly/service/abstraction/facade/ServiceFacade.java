package org.vitaly.service.abstraction.facade;

import org.vitaly.service.abstraction.factory.ServiceFactory;

/**
 * Created by vitaly on 18.04.17.
 */
public interface ServiceFacade {
    void init();

    ServiceFactory getServiceFactory();
}
