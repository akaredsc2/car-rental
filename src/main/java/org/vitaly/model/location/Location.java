package org.vitaly.model.location;

import org.vitaly.model.Entity;
import org.vitaly.model.car.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitaly on 2017-03-26.
 */
public class Location implements Entity {
    private long id;
    private String state;
    private String city;
    private String street;
    private String building;
    private String photoUrl;
    private List<Car> cars;

    private Location(Builder builder) {
        this.id = builder.id;
        this.state = builder.state;
        this.city = builder.city;
        this.street = builder.street;
        this.building = builder.building;
        this.photoUrl = builder.photoUrl;
        this.cars = builder.cars;
    }

    public Location() {
        this.id = -1;
        this.state = "";
        this.city = "";
        this.street = "";
        this.building = "";
        this.photoUrl = "";
        this.cars = new ArrayList<>();
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
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

        return state.equals(location.state)
                && city.equals(location.city)
                && street.equals(location.street)
                && building.equals(location.building);
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

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", building='" + building + '\'' +
                ", cars=" + cars +
                '}';
    }

    public static class Builder {
        private long id;
        private String state;
        private String city;
        private String street;
        private String building;
        private String photoUrl;
        private List<Car> cars;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setBuilding(String building) {
            this.building = building;
            return this;
        }

        public Builder setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Builder setCars(List<Car> cars) {
            this.cars = cars;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
