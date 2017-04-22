package org.vitaly.service.impl.dto;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModelDto {
    private long id;
    private String name;
    private String photoUrl;
    private int doorCount;
    private int seatCount;
    private int horsePowerCount;

    public long getId() {
        return id;
    }

    public CarModelDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CarModelDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public CarModelDto setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public int getDoorCount() {
        return doorCount;
    }

    public CarModelDto setDoorCount(int doorCount) {
        this.doorCount = doorCount;
        return this;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public CarModelDto setSeatCount(int seatCount) {
        this.seatCount = seatCount;
        return this;
    }

    public int getHorsePowerCount() {
        return horsePowerCount;
    }

    public CarModelDto setHorsePowerCount(int horsePowerCount) {
        this.horsePowerCount = horsePowerCount;
        return this;
    }
}
