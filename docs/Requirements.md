# Requirements

## Functional requirements

1. Register riders with a name and location.
2. Register drivers with a name, current location, and vehicle type.
3. List drivers who are currently available.
4. Request a ride for an existing rider using a trip distance and vehicle type.
5. Select an eligible available driver through a `RideMatchingStrategy`.
6. Calculate a fare through a `FareStrategy` when a ride is completed.
7. Track ride status using `REQUESTED`, `ASSIGNED`, `COMPLETED`, and `CANCELLED`.
8. Cancel requested or assigned rides.
9. Display all rides and their current state.
10. Allow the user to change matching and fare strategies at runtime.

## Non-functional requirements

- New fare strategies should be addable without modifying existing fare implementations.
- New driver matching strategies should be addable without modifying the ride-booking algorithm.
- Services should depend on focused interfaces and collaborators rather than concrete strategy implementations.
- Classes should have clear responsibilities and readable names.
- Invalid input and invalid state transitions should produce clear messages.
- The MVP should remain simple and console-based.

## Validation and business rules

- Rider and driver names must not be blank.
- Ride distance must be a finite positive number.
- A driver must be available and match the requested vehicle type to be assigned.
- A ride can be completed only when its status is `ASSIGNED`.
- A ride can be cancelled only when its status is `REQUESTED` or `ASSIGNED`.
- A driver is marked unavailable while assigned to a ride and available again after completion or cancellation.
- A fare receipt is generated at completion.

## Assumptions

- Rider and driver data are held in memory.
- Locations are represented as X/Y coordinates for a simple nearest-driver calculation.
- Fare values are illustrative and use the currency unit configured by the assignment; no currency conversion is performed.
- A single console session is the intended usage model.
