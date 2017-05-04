package org.vitaly.model.car;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by vitaly on 2017-03-26.
 */
public enum CarStateEnum {
    AVAILABLE(new AvailableState()),
    RESERVED(new ReservedState()),
    SERVED(new ServedState()),
    RETURNED(new ReturnedState()),
    UNAVAILABLE(new UnavailableState());

    private final CarState state;

    CarStateEnum(CarState state) {
        this.state = state;
    }

    public CarState getState() {
        return state;
    }

    public static Optional<CarState> stateOf(String stateName) {
        return Arrays.stream(CarStateEnum.values())
                .map(CarStateEnum::toString)
                .filter(string -> string.equalsIgnoreCase(stateName))
                .map(CarStateEnum::valueOf)
                .map(CarStateEnum::getState)
                .findFirst();
    }
}
