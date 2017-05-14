package org.vitaly.util;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum for supported locales
 */
public enum SupportedLocaleEnum {
    EN_US("en_US"),
    RU_RU("ru_RU"),
    UK_UA("uk_UA");
    private final String name;

    SupportedLocaleEnum(String name) {
        this.name = name;
    }

    /**
     * Name of locale
     *
     * @return name of locale
     */
    public String getName() {
        return name;
    }

    /**
     * Returns name of locale form enum or empty optional if no such locale present
     *
     * @param localeName locale name
     * @return name of locale form enum or empty optional if no such locale present
     */
    public static Optional<String> of(String localeName) {
        return Arrays.stream(SupportedLocaleEnum.values())
                .map(SupportedLocaleEnum::toString)
                .filter(string -> string.equalsIgnoreCase(localeName))
                .findFirst();
    }
}
