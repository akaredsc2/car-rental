package org.vitaly.data;

import org.vitaly.model.bill.Bill;
import org.vitaly.model.car.Car;
import org.vitaly.model.car.CarStateEnum;
import org.vitaly.model.location.Location;
import org.vitaly.model.notification.Notification;
import org.vitaly.model.notification.NotificationStatus;
import org.vitaly.model.user.User;
import org.vitaly.model.user.UserRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vitaly on 2017-04-08.
 */
public class TestData {
    private static TestData instance = new TestData();

    private Map<String, Car> carMap;
    private Map<String, Location> locationMap;
    private Map<String, User> userMap;
    private Map<String, Notification> notificationMap;
    private Map<String, Bill> billMap;

    private TestData() {
        carMap = fillWithCars();
        locationMap = fillWithLocations();
        userMap = fillWithUsers();
        notificationMap = fillWithNotifications();
        billMap = fillWithBills();
    }

    public static TestData getInstance() {
        return instance;
    }

    private Map<String, Car> fillWithCars() {
        Map<String, Car> result = new HashMap<>();

        Car car1 = new Car.Builder()
                .setId(1L)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setModel("Ford Focus")
                .setRegistrationPlate("666 satan 666")
                .setPhotoUrl("http://bit.ly/2o8TCb9")
                .setColor("grey")
                .setPricePerDay(BigDecimal.valueOf(100.0))
                .build();

        Car car2 = new Car.Builder()
                .setId(2L)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setModel("Ford Fiesta")
                .setRegistrationPlate("777 lucky 777")
                .setPhotoUrl("http://bit.ly/2mHkMc3")
                .setColor("blue")
                .setPricePerDay(BigDecimal.valueOf(120.0))
                .build();

        Car car3 = new Car.Builder()
                .setId(3L)
                .setState(CarStateEnum.UNAVAILABLE.getState())
                .setModel("Ford Fiesta")
                .setRegistrationPlate("13 unlucky 777")
                .setPhotoUrl("http://bit.ly/2mHkMc3")
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
                .setCars(new ArrayList<>())
                .build();

        Location location2 = new Location.Builder()
                .setId(5L)
                .setState("Odesska")
                .setCity("Odesa")
                .setStreet("Peysivska")
                .setBuilding("14")
                .setCars(new ArrayList<>())
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

    private Map<String, Notification> fillWithNotifications() {
        Map<String, Notification> result = new HashMap<>();

        Notification notification1 = new Notification.Builder()
                .setId(9L)
                .setCreationDateTime(LocalDateTime.now())
                .setStatus(NotificationStatus.NEW)
                .setHeader("header1")
                .setContent("content1")
                .build();

        Notification notification2 = new Notification.Builder()
                .setId(10L)
                .setCreationDateTime(LocalDateTime.now())
                .setStatus(NotificationStatus.NEW)
                .setHeader("header2")
                .setContent("content2")
                .build();

        result.put("notification1", notification1);
        result.put("notification2", notification2);

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

    public Car getCar(String name) {
        if (carMap.containsKey(name)) {
            Car storedCar = carMap.get(name);

            return new Car.Builder()
                    .setId(storedCar.getId())
                    .setState(storedCar.getState())
                    .setModel(storedCar.getModel())
                    .setRegistrationPlate(storedCar.getRegistrationPlate())
                    .setPhotoUrl(storedCar.getPhotoUrl())
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
                    .setCars(new ArrayList<>())
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

    public Notification getNotification(String name) {
        if (notificationMap.containsKey(name)) {
            Notification storedNotification = notificationMap.get(name);

            return new Notification.Builder()
                    .setId(storedNotification.getId())
                    .setCreationDateTime(storedNotification.getCreationDateTime())
                    .setStatus(storedNotification.getStatus())
                    .setHeader(storedNotification.getHeader())
                    .setContent(storedNotification.getContent())
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
