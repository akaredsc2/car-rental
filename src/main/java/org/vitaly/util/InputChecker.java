package org.vitaly.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility methods for input checks
 */
public class InputChecker {
    private static final Logger logger = LogManager.getLogger(InputChecker.class.getName());

    private InputChecker() {
    }

    /**
     * Checks if object is null
     *
     * @param object       object to check
     * @param errorMessage error message to log
     * @throws IllegalArgumentException if supplied object is null
     */
    public static void requireNotNull(Object object, String errorMessage) {
        if (object == null) {
            RuntimeException e = new IllegalArgumentException(errorMessage);
            logger.error("Null argument supplied", e);
            throw e;
        }
    }
}
