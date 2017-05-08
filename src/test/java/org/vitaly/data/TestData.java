package org.vitaly.data;

import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.carModel.CarModel;
import org.vitaly.model.location.Location;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vitaly on 2017-04-08.
 */
public class TestData {
    private static TestData instance = new TestData();

    private Map<String, CarModel> carModelMap;
    private Map<String, Car> carMap;
    private Map<String, Location> locationMap;
    private Map<String, User> userMap;
    private Map<String, Bill> billMap;

    private TestData() {
        carModelMap = fillWithCarModels();
        carMap = fillWithCars();
        locationMap = fillWithLocations();
        userMap = fillWithUsers();
        billMap = fillWithBills();
    }

    public static TestData getInstance() {
        return instance;
    }

    private Map<String, CarModel> fillWithCarModels() {
        Map<String, CarModel> result = new HashMap<>();

        CarModel carModel1 = new CarModel.Builder()
                .setId(135)
                .setName("Ford Focus")
                .setPhotoUrl("none")
                .setSeatCount(5)
                .setDoorCount(4)
                .setHorsePowerCount(150)
                .build();

        CarModel carModel2 = new CarModel.Builder()
                .setId(416)
                .setName("Ford Fiesta")
                .setPhotoUrl("none")
                .setSeatCount(2)
                .setDoorCount(2)
                .setHorsePowerCount(100)
                .build();

        result.put("carModel1", carModel1);
        result.put("carModel2", carModel2);

        return result;
    }

    private Map<String, Car> fillWithCars() {
        Map<String, Car> result = new HashMap<>();

        Car car1 = new Car.Builder()
                .setId(1L)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setCarModel(getCarModel("carModel1"))
                .setRegistrationPlate("666 satan 666")
                .setColor("grey")
                .setPricePerDay(BigDecimal.valueOf(100.0))
                .build();

        Car car2 = new Car.Builder()
                .setId(2L)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setCarModel(getCarModel("carModel2"))
                .setRegistrationPlate("777 lucky 777")
                .setColor("blue")
                .setPricePerDay(BigDecimal.valueOf(120.0))
                .build();

        Car car3 = new Car.Builder()
                .setId(3L)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setCarModel(getCarModel("carModel2"))
                .setRegistrationPlate("13 unlucky 777")
                .setColor("blue")
                .setPricePerDay(BigDecimal.valueOf(140.0))
                .build();

        result.put("car1", car1);
        result.put("car2", car2);
        result.put("car3", car3);

        return result;
    }

    private Map<String, Location> fillWithLocations() {
        Map<String, Location> result = new HashMap<>();

        Location location1 = new Location.Builder()
                .setId(4L)
                .setState("Kiev region")
                .setCity("Kotsjubinske")
                .setStreet("Ponomarjova")
                .setBuilding("18-a")
                .setPhotoUrl("location1url")
                .build();

        Location location2 = new Location.Builder()
                .setId(5L)
                .setState("Odesska")
                .setCity("Odesa")
                .setStreet("Peysivska")
                .setBuilding("14")
                .setPhotoUrl("location2url")
                .build();

        result.put("location1", location1);
        result.put("location2", location2);

        return result;
    }

    private Map<String, User> fillWithUsers() {
        Map<String, User> result = new HashMap<>();

        User client1 = new User.Builder()
                .setId(6L)
                .setLogin("vitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("ab123456")
                .setDriverLicenceNumber("123sdf456")
                .setRole(UserRole.CLIENT)
                .build();

        User client2 = new User.Builder()
                .setId(7L)
                .setLogin("evilVitaly")
                .setPassword("sh2r2p0v")
                .setFullName("Vitaly Victorovich Sharapov")
                .setBirthDate(LocalDate.of(1995, Month.AUGUST, 1))
                .setPassportNumber("666sat666")
                .setDriverLicenceNumber("1313an1313")
                .setRole(UserRole.CLIENT)
                .build();

        User admin = new User.Builder()
                .setId(8L)
                .setLogin("Karsa")
                .setPassword("toblakai")
                .setFullName("Karsa Orlong from Uryd Tribe")
                .setBirthDate(LocalDate.of(1937, Month.JANUARY, 1))
                .setPassportNumber("cd789101")
                .setDriverLicenceNumber("789def101")
                .setRole(UserRole.ADMIN)
                .build();

        result.put("client1", client1);
        result.put("client2", client2);
        result.put("admin", admin);

        return result;
    }

    private Map<String, Bill> fillWithBills() {
        Map<String, Bill> result = new HashMap<>();

        Bill bill1 = new Bill.Builder()
                .setPaid(false)
                .setDescription("description")
                .setCashAmount(BigDecimal.valueOf(1111))
                .setCreationDateTime(LocalDateTime.now())
                .build();

        Bill bill2 = new Bill.Builder()
                .setPaid(false)
                .setDescription("description2")
                .setCashAmount(BigDecimal.valueOf(2222))
                .setCreationDateTime(LocalDateTime.now())
                .build();

        result.put("bill1", bill1);
        result.put("bill2", bill2);

        return result;
    }

    public CarModel getCarModel(String name) {
        if (carModelMap.containsKey(name)) {
            CarModel storedCarModel = carModelMap.get(name);

            return new CarModel.Builder()
                    .setId(storedCarModel.getId())
                    .setName(storedCarModel.getName())
                    .setPhotoUrl(storedCarModel.getPhotoUrl())
                    .setDoorCount(storedCarModel.getDoorCount())
                    .setSeatCount(storedCarModel.getSeatCount())
                    .setHorsePowerCount(storedCarModel.getHorsePowerCount())
                    .build();
        }

        return null;
    }

    public Car getCar(String name) {
        if (carMap.containsKey(name)) {
            Car storedCar = carMap.get(name);

            return new Car.Builder()
                    .setId(storedCar.getId())
                    .setState(storedCar.getState())
                    .setCarModel(storedCar.getCarModel())
                    .setRegistrationPlate(storedCar.getRegistrationPlate())
                    .setColor(storedCar.getColor())
                    .setPricePerDay(storedCar.getPricePerDay())
                    .build();
        }

        return null;
    }

    public Location getLocation(String name) {
        if (locationMap.containsKey(name)) {
            Location storedLocation = locationMap.get(name);

            return new Location.Builder()
                    .setId(storedLocation.getId())
                    .setState(storedLocation.getState())
                    .setCity(storedLocation.getCity())
                    .setStreet(storedLocation.getStreet())
                    .setBuilding(storedLocation.getBuilding())
                    .setPhotoUrl(storedLocation.getPhotoUrl())
                    .build();
        }

        return null;
    }

    public User getUser(String name) {
        if (userMap.containsKey(name)) {
            User storedUser = userMap.get(name);

            return new User.Builder()
                    .setId(storedUser.getId())
                    .setLogin(storedUser.getLogin())
                    .setPassword(storedUser.getPassword())
                    .setFullName(storedUser.getFullName())
                    .setBirthDate(storedUser.getBirthDate())
                    .setPassportNumber(storedUser.getPassportNumber())
                    .setDriverLicenceNumber(storedUser.getDriverLicenceNumber())
                    .setRole(storedUser.getRole())
                    .build();
        }

        return null;
    }

    public Bill getBill(String name) {
        if (billMap.containsKey(name)) {
            Bill storedBill = billMap.get(name);

            return new Bill.Builder()
                    .setId(storedBill.getId())
                    .setPaid(storedBill.isPaid())
                    .setDescription(storedBill.getDescription())
                    .setCashAmount(storedBill.getCashAmount())
                    .setCreationDateTime(storedBill.getCreationDateTime())
                    .build();
        }

        return null;
    }
}
