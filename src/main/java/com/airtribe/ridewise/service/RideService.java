package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.EntityNotFoundException;
import com.airtribe.ridewise.exception.InvalidRideStateException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RideService {
    private final RiderService riderService;
    private final DriverService driverService;
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;
    private final Map<String, Ride> rides = new LinkedHashMap<>();

    public RideService(RiderService riderService, DriverService driverService,
                       RideMatchingStrategy rideMatchingStrategy, FareStrategy fareStrategy) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(String riderId, double distance, VehicleType vehicleType) {
        if (distance <= 0 || !Double.isFinite(distance)) {
            throw new IllegalArgumentException("Distance must be a positive number");
        }

        Rider rider = riderService.getRiderById(riderId);
        Ride ride = new Ride(IdGenerator.nextRideId(), rider, distance, vehicleType);
        rides.put(ride.getId(), ride);

        Driver driver = rideMatchingStrategy.findDriver(
                rider, driverService.getAvailableDrivers(), vehicleType);

        if (driver == null) {
            rides.remove(ride.getId());
            throw new NoDriverAvailableException("No available " + vehicleType + " driver was found");
        }

        ride.setDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);
        driver.setAvailable(false);
        driver.incrementActiveRideCount();
        return ride;
    }

    public FareReceipt completeRide(String rideId) {
        Ride ride = getRideById(rideId);
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStateException("Only assigned rides can be completed");
        }

        double amount = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(ride.getId(), amount);
        ride.setFareReceipt(receipt);
        ride.setStatus(RideStatus.COMPLETED);
        ride.getDriver().decrementActiveRideCount();
        ride.getDriver().setAvailable(true);
        return receipt;
    }

    public void cancelRide(String rideId) {
        Ride ride = getRideById(rideId);
        if (ride.getStatus() != RideStatus.REQUESTED && ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStateException("Only requested or assigned rides can be cancelled");
        }
        if (ride.getDriver() != null) {
            ride.getDriver().decrementActiveRideCount();
            ride.getDriver().setAvailable(true);
        }
        ride.setStatus(RideStatus.CANCELLED);
    }

    public Ride getRideById(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            throw new EntityNotFoundException("Ride not found: " + rideId);
        }
        return ride;
    }

    public List<Ride> getAllRides() {
        return List.copyOf(new ArrayList<>(rides.values()));
    }

    public void setRideMatchingStrategy(RideMatchingStrategy rideMatchingStrategy) {
        this.rideMatchingStrategy = rideMatchingStrategy;
    }

    public void setFareStrategy(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }
}
