package org.vitaly.model.carModel;

import org.vitaly.model.Entity;

/**
 * Created by vitaly on 2017-04-22.
 */
public class CarModel implements Entity {
    private long id;
    private String name;
    private String photoUrl;
    private int doorCount;
    private int seatCount;
    private int horsePowerCount;

    private CarModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.photoUrl = builder.photoUrl;
        this.doorCount = builder.doorCount;
        this.seatCount = builder.seatCount;
        this.horsePowerCount = builder.horsePowerCount;
    }

    @Override
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getDoorCount() {
        return doorCount;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public int getHorsePowerCount() {
        return horsePowerCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarModel carModel = (CarModel) o;

        return doorCount == carModel.doorCount
                && seatCount == carModel.seatCount
                && horsePowerCount == carModel.horsePowerCount
                && name.equals(carModel.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + doorCount;
        result = 31 * result + seatCount;
        result = 31 * result + horsePowerCount;
        return result;
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", doorCount=" + doorCount +
                ", seatCount=" + seatCount +
                ", horsePowerCount=" + horsePowerCount +
                '}';
    }

    public static class Builder {
        private long id;
        private String name;
        private String photoUrl;
        private int doorCount;
        private int seatCount;
        private int horsePowerCount;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Builder setDoorCount(int doorCount) {
            this.doorCount = doorCount;
            return this;
        }

        public Builder setSeatCount(int seatCount) {
            this.seatCount = seatCount;
            return this;
        }

        public Builder setHorsePowerCount(int horsePowerCount) {
            this.horsePowerCount = horsePowerCount;
            return this;
        }

        public CarModel build() {
            return new CarModel(this);
        }
    }

    public static CarModel createDummyCarModelWithId(long id) {
        return new CarModel.Builder()
                .setId(id)
                .setName("")
                .setPhotoUrl("")
                .setDoorCount(0)
                .setSeatCount(0)
                .setHorsePowerCount(0)
                .build();
    }
}
