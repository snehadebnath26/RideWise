package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final RiderService riderService = new RiderService();
    private final DriverService driverService = new DriverService();
    private final RideService rideService = new RideService(
            riderService, driverService, new NearestDriverStrategy(), new DefaultFareStrategy());

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addRider();
                    case 2 -> addDriver();
                    case 3 -> viewAvailableDrivers();
                    case 4 -> requestRide();
                    case 5 -> completeRide();
                    case 6 -> viewRides();
                    case 7 -> cancelRide();
                    case 8 -> changeMatchingStrategy();
                    case 9 -> changeFareStrategy();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option. Choose a listed menu number.");
                }
            } catch (RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
            System.out.println();
        }
        System.out.println("Thank you for using RideWise.");
    }

    private void printMenu() {
        System.out.println("========== RideWise ==========");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Cancel Ride");
        System.out.println("8. Change Driver Matching Strategy");
        System.out.println("9. Change Fare Strategy");
        System.out.println("0. Exit");
    }

    private void addRider() {
        String name = readText("Rider name: ");
        Location location = readLocation();
        Rider rider = riderService.registerRider(name, location);
        System.out.println("Rider registered: " + rider);
    }

    private void addDriver() {
        String name = readText("Driver name: ");
        Location location = readLocation();
        VehicleType vehicleType = readVehicleType();
        Driver driver = driverService.registerDriver(name, location, vehicleType);
        System.out.println("Driver registered: " + driver);
    }

    private void viewAvailableDrivers() {
        var drivers = driverService.getAvailableDrivers();
        if (drivers.isEmpty()) {
            System.out.println("No available drivers.");
            return;
        }
        drivers.forEach(System.out::println);
    }

    private void requestRide() {
        String riderId = readText("Rider ID: ");
        double distance = readDouble("Trip distance (km): ");
        VehicleType vehicleType = readVehicleType();
        Ride ride = rideService.requestRide(riderId, distance, vehicleType);
        System.out.println("Ride booked successfully: " + ride);
    }

    private void completeRide() {
        String rideId = readText("Ride ID: ");
        FareReceipt receipt = rideService.completeRide(rideId);
        System.out.println("Ride completed. " + receipt);
    }

    private void viewRides() {
        var rides = rideService.getAllRides();
        if (rides.isEmpty()) {
            System.out.println("No rides have been requested.");
            return;
        }
        rides.forEach(System.out::println);
    }

    private void cancelRide() {
        String rideId = readText("Ride ID: ");
        rideService.cancelRide(rideId);
        System.out.println("Ride cancelled: " + rideId);
    }

    private void changeMatchingStrategy() {
        System.out.println("1. Nearest driver");
        System.out.println("2. Least active driver");
        int choice = readInt("Choose matching strategy: ");
        RideMatchingStrategy strategy = switch (choice) {
            case 1 -> new NearestDriverStrategy();
            case 2 -> new LeastActiveDriverStrategy();
            default -> throw new IllegalArgumentException("Unknown matching strategy");
        };
        rideService.setRideMatchingStrategy(strategy);
        System.out.println("Matching strategy updated.");
    }

    private void changeFareStrategy() {
        System.out.println("1. Default fare");
        System.out.println("2. Peak-hour fare (1.5x)");
        int choice = readInt("Choose fare strategy: ");
        switch (choice) {
            case 1 -> rideService.setFareStrategy(new DefaultFareStrategy());
            case 2 -> rideService.setFareStrategy(new PeakHourFareStrategy());
            default -> throw new IllegalArgumentException("Unknown fare strategy");
        }
        System.out.println("Fare strategy updated.");
    }

    private Location readLocation() {
        double x = readDouble("Location X coordinate: ");
        double y = readDouble("Location Y coordinate: ");
        return new Location(x, y);
    }

    private VehicleType readVehicleType() {
        while (true) {
            String value = readText("Vehicle type (BIKE/AUTO/CAR): ").toUpperCase();
            try {
                return VehicleType.valueOf(value);
            } catch (IllegalArgumentException exception) {
                System.out.println("Enter BIKE, AUTO, or CAR.");
            }
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (Double.isFinite(value)) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Enter a valid number.");
        }
    }
}
