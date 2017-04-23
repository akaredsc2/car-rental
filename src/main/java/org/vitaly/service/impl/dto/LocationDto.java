package org.vitaly.service.impl.dto;

import java.util.List;
import java.util.Objects;

/**
 * Created by vitaly on 2017-04-20.
 */
public class LocationDto {
    private long id;
    private String state;
    private String city;
    private String street;
    private String building;
    private String photoUrl;
    private List<CarDto> carDtoList;

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

    public List<CarDto> getCarDtoList() {
        return carDtoList;
    }

    public void setCarDtoList(List<CarDto> carDtoList) {
        this.carDtoList = carDtoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationDto that = (LocationDto) o;

        return id == that.id
                && Objects.equals(state, that.state)
                && Objects.equals(city, that.city)
                && Objects.equals(street, that.street)
                && Objects.equals(building, that.building)
                && Objects.equals(photoUrl, that.photoUrl)
                && Objects.equals(carDtoList, that.carDtoList);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (building != null ? building.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + (carDtoList != null ? carDtoList.hashCode() : 0);
        return result;
    }
}
