package org.vitaly.model.car;

import org.vitaly.model.Entity;
import org.vitaly.model.carModel.CarModel;

import java.math.BigDecimal;

/**
 * Car in system. Use Car.Builder to create instances or static factory method
 *
 * @see Car.Builder
 * @see Car#createDummyCarWithId(long)
 */
public class Car implements Entity {
    private long id;
    private CarState state;
    private CarModel carModel;
    private String registrationPlate;
    private String color;
    private BigDecimal pricePerDay;

    private Car(Builder builder) {
        this.id = builder.id;
        this.state = builder.state;
        this.carModel = builder.carModel;
        this.registrationPlate = builder.registrationPlate;
        this.color = builder.color;
        this.pricePerDay = builder.pricePerDay;
    }

    /**
     * Id of car
     *
     * @return id of car
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * State of car
     *
     * @return state of car
     */
    public CarState getState() {
        return state;
    }

    /**
     * Sets state of car. Part of State design pattern
     *
     * @param state
     */
    void setState(CarState state) {
        this.state = state;
    }

    /**
     * Model of car
     *
     * @return model of car
     */
    public CarModel getCarModel() {
        return carModel;
    }

    /**
     * Registration plate of car
     *
     * @return registration plate of car
     */
    public String getRegistrationPlate() {
        return registrationPlate;
    }

    /**
     * Color of car
     *
     * @return color of car
     */
    public String getColor() {
        return color;
    }

    /**
     * Price per day of using car
     *
     * @return price per day of using car
     */
    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    /**
     * Try to make car available
     *
     * @return true if made available, false otherwise
     */
    public boolean makeAvailable() {
        return state.makeAvailable(this);
    }

    /**
     * Try to make car unavailable
     *
     * @return true if made unavailable, false otherwise
     */
    public boolean makeUnavailable() {
        return state.makeUnavailable(this);
    }

    /**
     * Try to make car reserved
     *
     * @return true if made reserved, false otherwise
     */
    public boolean reserve() {
        return state.reserve(this);
    }

    /**
     * Try to make car served
     *
     * @return true if made served, false otherwise
     */
    public boolean serve() {
        return state.serve(this);
    }

    /**
     * Try to make car returned
     *
     * @return true if made returned, false otherwise
     */
    public boolean doReturn() {
        return state.doReturn(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Car car = (Car) o;

        return state.equals(car.state)
                && carModel.getId() == car.carModel.getId()
                && registrationPlate.equals(car.registrationPlate)
                && color.equals(car.color)
                && pricePerDay.stripTrailingZeros().equals(car.pricePerDay.stripTrailingZeros());
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + carModel.hashCode();
        result = 31 * result + registrationPlate.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    /**
     * Creates instances of Car with supplied parameters
     *
     * @see Car
     */
    public static class Builder {
        private long id;
        private CarState state;
        private CarModel carModel;
        private String registrationPlate;
        private String color;
        private BigDecimal pricePerDay;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setState(CarState state) {
            this.state = state;
            return this;
        }

        public Builder setCarModel(CarModel carModel) {
            this.carModel = carModel;
            return this;
        }

        public Builder setRegistrationPlate(String registrationPlate) {
            this.registrationPlate = registrationPlate;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setPricePerDay(BigDecimal pricePerDay) {
            this.pricePerDay = pricePerDay;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }

    /**
     * Static factory method to create car with supplied id
     *
     * @param id id
     * @return Car with supplied id
     */
    public static Car createDummyCarWithId(long id) {
        return new Car.Builder()
                .setId(id)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setCarModel(CarModel.createDummyCarModelWithId(-1))
                .setRegistrationPlate("")
                .setColor("")
                .setPricePerDay(BigDecimal.ZERO)
                .build();
    }
}
