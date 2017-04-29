package org.vitaly.util;

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
}
