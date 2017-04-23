package org.vitaly.service.impl.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarModelDto that = (CarModelDto) o;

        return id == that.id
                && doorCount == that.doorCount
                && seatCount == that.seatCount
                && horsePowerCount == that.horsePowerCount
                && Objects.equals(name, that.name)
                && Objects.equals(photoUrl, that.photoUrl);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + doorCount;
        result = 31 * result + seatCount;
        result = 31 * result + horsePowerCount;
        return result;
    }
}
