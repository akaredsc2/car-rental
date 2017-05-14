package org.vitaly.model.car;

/**
 * Available car state
 */
public class AvailableState extends CarState {
    AvailableState() {
    }

    /**
     * @inheritDoc
     */
    @Override
    boolean makeUnavailable(Car car) {
        car.setState(CarStateEnum.UNAVAILABLE.getState());
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    boolean reserve(Car car) {
        car.setState(CarStateEnum.RESERVED.getState());
        return true;
    }

    @Override
    public String toString() {
        return "available";
    }
}
