package org.vitaly.model.location;

import org.vitaly.model.Entity;

import java.util.Objects;

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

    private Location(Builder builder) {
        this.id = builder.id;
        this.state = builder.state;
        this.city = builder.city;
        this.street = builder.street;
        this.building = builder.building;
        this.photoUrl = builder.photoUrl;
    }

    @Override
    public long getId() {
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

    public String getPhotoUrl() {
        return photoUrl;
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

        return Objects.equals(state, location.state)
                && Objects.equals(city, location.city)
                && Objects.equals(street, location.street)
                && Objects.equals(building, location.building)
                && Objects.equals(photoUrl, location.photoUrl);
    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (building != null ? building.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
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
                '}';
    }

    public static class Builder {
        private long id;
        private String state;
        private String city;
        private String street;
        private String building;
        private String photoUrl;

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

        public Location build() {
            return new Location(this);
        }
    }
}
