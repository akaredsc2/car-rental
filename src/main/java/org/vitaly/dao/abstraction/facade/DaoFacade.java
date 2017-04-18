package org.vitaly.dao.abstraction.facade;

import org.vitaly.dao.abstraction.factory.TransactionFactory;

/**
 * Created by vitaly on 18.04.17.
 */
public interface DaoFacade {
    void init();

    TransactionFactory getTransactionFactory();
}
