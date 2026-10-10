package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import java.util.Comparator;
import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers, VehicleType vehicleType) {
        return drivers.stream()
                .filter(Driver::isAvailable)
                .filter(driver -> driver.getVehicleType() == vehicleType)
                .min(Comparator.comparingDouble(driver ->
                        rider.getLocation().distanceTo(driver.getCurrentLocation())))
                .orElse(null);
    }
}
