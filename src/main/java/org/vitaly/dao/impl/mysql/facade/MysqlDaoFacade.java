package org.vitaly.dao.impl.mysql.facade;

import org.vitaly.dao.abstraction.connectionPool.ConnectionPool;
import org.vitaly.dao.abstraction.facade.DaoFacade;
import org.vitaly.dao.abstraction.factory.TransactionFactory;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;
import org.vitaly.dao.impl.mysql.factory.MapperFactory;
import org.vitaly.dao.impl.mysql.factory.MysqlTransactionFactory;

/**
 * Created by vitaly on 18.04.17.
 */
public class MysqlDaoFacade implements DaoFacade {
    private static MysqlDaoFacade instance = new MysqlDaoFacade();

    private TransactionFactory transactionFactory;

    private MysqlDaoFacade() {
        init();
    }

    public static MysqlDaoFacade getInstance() {
        return instance;
    }

    @Override
    public void init() {
        ConnectionPool connectionPool = MysqlConnectionPool.getInstance();
        MapperFactory mapperFactory = new MapperFactory();
        this.transactionFactory = new MysqlTransactionFactory(connectionPool, mapperFactory);
    }

    @Override
    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }
}
