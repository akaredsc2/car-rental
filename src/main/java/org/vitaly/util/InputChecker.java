package org.vitaly.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created by vitaly on 2017-03-26.
 */
public class InputChecker {
    private static final Logger logger = LogManager.getLogger(InputChecker.class.getName());

    private InputChecker() {
    }

    public static void requireNotNull(Object object, String errorMessage) {
        if (object == null) {
            RuntimeException e = new IllegalArgumentException(errorMessage);
            logger.error("Null argument supplied", e);
            throw e;
        }
    }
}
