package org.vitaly.model.carModel;

import org.vitaly.model.Entity;

/**
 * Model of car. Use CarModel.Builder to create instances or static factory method
 *
 * @see CarModel.Builder
 * @see CarModel#createDummyCarModelWithId(long)
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

    /**
     * Id of model
     *
     * @return id of model
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * Name of model
     *
     * @return name of model
     */
    public String getName() {
        return name;
    }

    /**
     * Url to photo of model
     *
     * @return url to photo of model
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * Door count
     *
     * @return door count
     */
    public int getDoorCount() {
        return doorCount;
    }

    /**
     * Seat count
     *
     * @return seat count
     */
    public int getSeatCount() {
        return seatCount;
    }

    /**
     * Horse power count of engine
     *
     * @return horse power count of engine
     */
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

    /**
     * Class that creates instances of CarModel class with supplied parameters
     *
     * @see CarModel
     */
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

    /**
     * Static factory method to create car model with supplied id
     *
     * @param id id of car model
     * @return car model with supplied id
     */
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
