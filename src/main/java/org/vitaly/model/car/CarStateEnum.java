package org.vitaly.model.car;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds instances of car states
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

    /**
     * Car state
     *
     * @return car state
     */
    public CarState getState() {
        return state;
    }

    /**
     * Returns corresponding car state or empty optional otherwise
     *
     * @param stateName state name
     * @return corresponding car state or empty optional otherwise
     */
    public static Optional<CarState> stateOf(String stateName) {
        return Arrays.stream(CarStateEnum.values())
                .map(CarStateEnum::toString)
                .filter(string -> string.equalsIgnoreCase(stateName))
                .map(CarStateEnum::valueOf)
                .map(CarStateEnum::getState)
                .findFirst();
    }
}
