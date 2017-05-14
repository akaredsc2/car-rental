package org.vitaly.model.car;

/**
 * Served car state
 */
public class ServedState extends CarState {
    ServedState() {
    }

    /**
     * @inheritDoc
     */
    @Override
    boolean doReturn(Car car) {
        car.setState(CarStateEnum.RETURNED.getState());
        return true;
    }

    @Override
    public String toString() {
        return "served";
    }
}
