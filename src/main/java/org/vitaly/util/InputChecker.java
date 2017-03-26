package org.vitaly.util;

/**
 * Created by vitaly on 2017-03-26.
 */
public class InputChecker {
    private InputChecker() {
    }

    public static void requireNotNull(Object object, String errorMessage) {
        if (object == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
