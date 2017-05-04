package org.vitaly.dao.impl.mysql.transaction;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.vitaly.dao.impl.mysql.connectionPool.MysqlConnectionPool;

/**
 * Created by vitaly on 2017-04-17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MysqlConnectionPool.class)
@PowerMockIgnore("javax.management.*")
public class TransactionManagerTest {

}