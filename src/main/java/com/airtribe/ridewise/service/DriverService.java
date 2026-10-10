package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.EntityNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DriverService {
    private final Map<String, Driver> drivers = new LinkedHashMap<>();

    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Driver name cannot be empty");
        }
        Driver driver = new Driver(IdGenerator.nextDriverId(), name.trim(), location, vehicleType);
        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Driver getDriverById(String id) {
        Driver driver = drivers.get(id);
        if (driver == null) {
            throw new EntityNotFoundException("Driver not found: " + id);
        }
        return driver;
    }

    public void updateAvailability(String driverId, boolean available) {
        getDriverById(driverId).setAvailable(available);
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers.values()) {
            if (driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }
        return List.copyOf(availableDrivers);
    }

    public List<Driver> getAllDrivers() {
        return List.copyOf(drivers.values());
    }
}
