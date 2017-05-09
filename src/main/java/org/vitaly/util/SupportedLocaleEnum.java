package org.vitaly.util;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by vitaly on 29.04.17.
 */
public enum SupportedLocaleEnum {
    EN_US("en_US"),
    RU_RU("ru_RU"),
    UK_UA("uk_UA");
    private final String name;

    SupportedLocaleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<String> of(String localeName) {
        return Arrays.stream(SupportedLocaleEnum.values())
                .map(SupportedLocaleEnum::toString)
                .filter(string -> string.equalsIgnoreCase(localeName))
                .findFirst();
    }
}
