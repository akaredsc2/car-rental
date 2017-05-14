package org.vitaly.model.car;

/**
 * Unavailable car state
 */
public class UnavailableState extends CarState {
    UnavailableState() {
    }

    /**
     * @inheritDoc
     */
    @Override
    boolean makeAvailable(Car car) {
        car.setState(CarStateEnum.AVAILABLE.getState());
        return true;
    }

    @Override
    public String toString() {
        return "unavailable";
    }
}