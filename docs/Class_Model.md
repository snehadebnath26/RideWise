# Class Model

## Main classes

| Class | Responsibility |
|---|---|
| `Rider` | Stores rider identity, name, and location. |
| `Driver` | Stores driver identity, name, current location, vehicle type, availability, and active ride count. |
| `Ride` | Associates a rider with an assigned driver, trip distance, vehicle type, status, and optional fare receipt. |
| `FareReceipt` | Stores the ride ID, final fare amount, and generation timestamp. |
| `Location` | Stores X/Y coordinates and calculates coordinate distance. |
| `RiderService` | Registers riders and retrieves riders by ID. |
| `DriverService` | Registers drivers, retrieves drivers, changes availability, and lists available drivers. |
| `RideService` | Orchestrates booking, assignment, completion, cancellation, fare receipt creation, and ride lookup. |
| `IdGenerator` | Creates unique in-session rider, driver, and ride identifiers. |

## Strategy interfaces

### `RideMatchingStrategy`

Contract: `findDriver(Rider rider, List<Driver> drivers, VehicleType vehicleType)`

Implementations:
- `NearestDriverStrategy`: selects the nearest available driver with the requested vehicle type.
- `LeastActiveDriverStrategy`: selects the eligible driver with the lowest active ride count, using proximity as a tie-breaker.

### `FareStrategy`

Contract: `calculateFare(Ride ride)`

Implementations:
- `DefaultFareStrategy`: applies a base fare and vehicle-specific per-kilometre rate.
- `PeakHourFareStrategy`: decorates a base fare strategy and applies a configurable multiplier.

## Design decisions

- `RideService` uses constructor injection for `RiderService`, `DriverService`, `RideMatchingStrategy`, and `FareStrategy`.
- Strategy interfaces isolate changeable algorithms from ride lifecycle orchestration.
- `Location` is a small value-like class that encapsulates coordinate distance calculation.
- In-memory maps are sufficient for the assignment's console MVP.
