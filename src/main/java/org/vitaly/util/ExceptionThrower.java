package org.vitaly.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vitaly on 2017-04-08.
 */
public class ExceptionThrower {
    private static Logger logger = LogManager.getLogger(ExceptionThrower.class.getName());

    private ExceptionThrower() {
    }

    public static void unsupported() {
        RuntimeException e = new UnsupportedOperationException();
        logger.error("Error while calling unsupported operation.", e);
        throw e;
    }
}
