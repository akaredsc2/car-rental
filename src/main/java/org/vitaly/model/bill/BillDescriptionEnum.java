package org.vitaly.model.bill;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by vitaly on 09.05.17.
 */

/**
 * Description of bill
 */
public enum BillDescriptionEnum {
    SERVICE,
    DAMAGE;

    /**
     * Returns bill description from supplied string of empty optional otherwise
     *
     * @param description description
     * @return bill description from supplied string of empty optional otherwise
     */
    public static Optional<BillDescriptionEnum> of(String description) {
        return Arrays.stream(BillDescriptionEnum.values())
                .map(BillDescriptionEnum::toString)
                .filter(string -> string.equalsIgnoreCase(description))
                .map(BillDescriptionEnum::valueOf)
                .findFirst();
    }
}
