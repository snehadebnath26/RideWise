# SOLID Principles Reflection

## Single Responsibility Principle (SRP)

Each class has a focused responsibility. `RiderService` manages riders, `DriverService` manages drivers, and `RideService` coordinates the ride lifecycle. Fare algorithms belong to `FareStrategy` implementations, while `IdGenerator` handles ID creation.

## Open/Closed Principle (OCP)

The system is open to extension through strategy interfaces. A new matching algorithm can implement `RideMatchingStrategy`; a new pricing algorithm can implement `FareStrategy`. The ride workflow does not need to know the details of each algorithm.

## Liskov Substitution Principle (LSP)

`NearestDriverStrategy` and `LeastActiveDriverStrategy` can be used wherever a `RideMatchingStrategy` is expected. Likewise, `DefaultFareStrategy` and `PeakHourFareStrategy` satisfy the `FareStrategy` contract by calculating a fare for a ride. Implementations must preserve these contracts and avoid surprising callers with incompatible behavior.

## Interface Segregation Principle (ISP)

Matching and pricing are represented by two small interfaces. A pricing implementation is not forced to provide driver selection methods, and a matching implementation is not forced to calculate fares.

## Dependency Inversion Principle (DIP)

`RideService` depends on the abstractions `RideMatchingStrategy` and `FareStrategy`, not on `NearestDriverStrategy` or `DefaultFareStrategy` directly. The selected implementations are passed into the constructor and can be changed at runtime.

## Other design principles

- **DRY:** the service delegates matching to one strategy contract instead of embedding repeated driver-selection logic.
- **KISS:** domain entities contain only fields and behavior required by the MVP.
- **YAGNI:** the project does not add a database, authentication, payment processing, external maps, or distributed services.
- **Law of Demeter:** the console layer calls service methods. It does not perform matching or fare calculation itself; collaborators are accessed through their owning service.
- **Composition over inheritance:** `RideService` is composed with service and strategy objects. `PeakHourFareStrategy` composes a base `FareStrategy` rather than inheriting a concrete fare algorithm.
