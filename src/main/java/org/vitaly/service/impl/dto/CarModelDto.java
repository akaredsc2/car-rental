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

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getDoorCount() {
        return doorCount;
    }

    public void setDoorCount(int doorCount) {
        this.doorCount = doorCount;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public int getHorsePowerCount() {
        return horsePowerCount;
    }

    public void setHorsePowerCount(int horsePowerCount) {
        this.horsePowerCount = horsePowerCount;
    }
}
