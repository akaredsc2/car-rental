package org.vitaly.service.impl.dto;

import org.vitaly.model.car.CarState;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by vitaly on 2017-04-20.
 */
public class CarDto {
    private long id;
    private CarState state;
    private CarModelDto carModel;
    private String registrationPlate;
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

    public CarModelDto getCarModelDto() {
        return carModel;
    }

    public void setCarModelDto(CarModelDto carModel) {
        this.carModel = carModel;
    }

    public void setState(CarState state) {
        this.state = state;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarDto carDto = (CarDto) o;

        return id == carDto.id
                && Objects.equals(state, carDto.state)
                && Objects.equals(carModel, carDto.carModel)
                && Objects.equals(registrationPlate, carDto.registrationPlate)
                && Objects.equals(color, carDto.color)
                && Objects.equals(pricePerDay, carDto.pricePerDay);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (carModel != null ? carModel.hashCode() : 0);
        result = 31 * result + (registrationPlate != null ? registrationPlate.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (pricePerDay != null ? pricePerDay.hashCode() : 0);
        return result;
    }
}
