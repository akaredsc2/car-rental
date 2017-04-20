package org.vitaly.service.impl.dto;

import org.vitaly.model.car.CarState;

import java.math.BigDecimal;

/**
 * Created by vitaly on 2017-04-20.
 */
public class CarDto {
    private long id;
    private CarState state;
    private String model;
    private String registrationPlate;
    private String photoUrl;
    private String color;
    private BigDecimal pricePerDay;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CarState getState() {
        return state;
    }

    public void setState(CarState state) {
        this.state = state;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
