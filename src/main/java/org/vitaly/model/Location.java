package org.vitaly.model;

import java.util.ArrayList;
import java.util.List;

import static org.vitaly.util.InputChecker.requireNotNull;

/**
 * Created by vitaly on 2017-03-26.
 */
public class Location {
    private Long id;
    private String state;
    private String city;
    private String street;
    private String building;
    private List<Car> cars;

    private Location(Builder builder) {
        this.id = builder.id;
        this.state = builder.state;
        this.city = builder.city;
        this.street = builder.street;
        this.building = builder.building;
        this.cars = builder.cars;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getBuilding() {
        return building;
    }

    public List<Car> getCars() {
        return new ArrayList<>(cars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        if (!state.equals(location.state)) {
            return false;
        }
        if (!city.equals(location.city)) {
            return false;
        }
        if (!street.equals(location.street)) {
            return false;
        }
        if (!building.equals(location.building)) {
            return false;
        }
        return cars.equals(location.cars);
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + building.hashCode();
        result = 31 * result + cars.hashCode();
        return result;
    }

    public static class Builder {
        private Long id;
        private String state;
        private String city;
        private String street;
        private String building;
        private List<Car> cars;

        public Builder() {
        }

        public Builder setId(Long id) {
            requireNotNull(id, "Id must not be null!");

            this.id = id;
            return this;
        }

        public Builder setState(String state) {
            requireNotNull(state, "State must not be null!");

            this.state = state;
            return this;
        }

        public Builder setCity(String city) {
            requireNotNull(city, "City must not be null!");

            this.city = city;
            return this;
        }

        public Builder setStreet(String street) {
            requireNotNull(street, "Street must not be null!");

            this.street = street;
            return this;
        }

        public Builder setBuilding(String building) {
            requireNotNull(building, "Building must not be null!");

            this.building = building;
            return this;
        }

        public Builder setCars(List<Car> cars) {
            requireNotNull(cars, "Cars must not be null!");

            this.cars = cars;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
