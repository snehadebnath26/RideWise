# Object Relationships

## Relationships from the brief

| Relationship | Type | Explanation |
|---|---|---|
| Rider → Ride | Association | A ride references the rider who requested it. |
| Driver → Ride | Association | An assigned ride references its driver. |
| Ride → FareReceipt | Composition-style ownership | A ride holds its generated receipt, which is created when the ride is completed. |
| RideService → Strategies | Composition / dependency injection | The service is constructed with strategy objects and delegates algorithm decisions to them. |

## Additional relationships

- `RideService` depends on `RiderService` and `DriverService` to retrieve domain entities and available drivers.
- `Rider` has a `Location`.
- `Driver` has a current `Location` and a `VehicleType`.
- `Ride` has a `VehicleType` and a `RideStatus`.
- `NearestDriverStrategy` and `LeastActiveDriverStrategy` implement `RideMatchingStrategy`.
- `DefaultFareStrategy` and `PeakHourFareStrategy` implement `FareStrategy`.
- `PeakHourFareStrategy` contains a reference to another `FareStrategy`, allowing it to apply a multiplier to the base fare.

## Lifecycle

`REQUESTED` → `ASSIGNED` → `COMPLETED`

A requested or assigned ride can instead transition to `CANCELLED`. Completed and cancelled rides are terminal states in this MVP.
