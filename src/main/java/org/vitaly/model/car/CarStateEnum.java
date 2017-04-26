package org.vitaly.model.car;

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
}
