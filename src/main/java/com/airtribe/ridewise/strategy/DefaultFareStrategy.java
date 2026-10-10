package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.VehicleType;

public class DefaultFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 30.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + ride.getDistance() * rateFor(ride.getVehicleType());
    }

    private double rateFor(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> 8.0;
            case AUTO -> 12.0;
            case CAR -> 18.0;
        };
    }
}
