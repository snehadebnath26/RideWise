# RideWise — Modular Ride-Sharing System

RideWise is a console-based Java application that demonstrates low-level design (LLD), the Strategy Pattern, SOLID principles, composition over inheritance, and separation of concerns. It models a small ride-sharing workflow without introducing unnecessary infrastructure.

## Features

- Register riders and drivers.
- Store rider and driver locations as X/Y coordinates.
- View available drivers.
- Request rides by rider ID, distance, and vehicle type.
- Match drivers with either nearest-driver or least-active-driver strategies.
- Calculate fares with default or peak-hour pricing.
- Complete and cancel rides.
- Track ride status: `REQUESTED`, `ASSIGNED`, `COMPLETED`, and `CANCELLED`.
- Generate a fare receipt when a ride is completed.
- Change matching and fare strategies at runtime.
- Validate console input and report domain errors clearly.

## Requirements

- JDK 17 or later
- Maven 3.8 or later (optional; useful for packaging)
- IntelliJ IDEA or another Java IDE (optional)

## Run the application

From the project root:

```bash
mvn clean package
java -cp target/classes com.airtribe.ridewise.Main
```

Alternatively, in IntelliJ IDEA, open the folder containing `pom.xml`, wait for Maven import, then run `src/main/java/com/airtribe/ridewise/Main.java`.

## Menu

1. Add Rider
2. Add Driver
3. View Available Drivers
4. Request Ride
5. Complete Ride
6. View Rides
7. Cancel Ride
8. Change Driver Matching Strategy
9. Change Fare Strategy
0. Exit

## How locations work

The MVP uses two-dimensional X/Y coordinates to keep location modeling simple and make nearest-driver selection demonstrable. The distance between coordinates is Euclidean distance. The trip distance entered during ride booking is used for fare calculation; it is independent of the coordinate distance used for matching.

## Fare rules

The default fare is `base fare + (trip distance × vehicle rate)`:

| Vehicle | Base fare | Rate per km |
|---|---:|---:|
| BIKE | 30 | 8 |
| AUTO | 30 | 12 |
| CAR | 30 | 18 |

Peak-hour pricing multiplies the default fare by `1.5`. These are illustrative assignment values, not real-world prices.

## Design overview

- `model/`: domain entities and enums.
- `strategy/`: interfaces and interchangeable matching/pricing algorithms.
- `service/`: rider, driver, and ride use cases.
- `exception/`: meaningful domain exceptions.
- `util/`: ID generation.
- `docs/`: requirements, class model, SOLID reflection, and object relationships.

## SOLID and design principles

- **SRP:** rider registration, driver management, ride orchestration, fare calculation, matching, and ID generation are separated.
- **OCP:** new `RideMatchingStrategy` and `FareStrategy` implementations can be introduced without changing the matching/pricing algorithms inside `RideService`.
- **LSP:** each strategy implementation honors the contract of its interface and can replace another implementation.
- **ISP:** matching and pricing use separate, focused interfaces.
- **DIP:** `RideService` receives strategy interfaces and service collaborators through its constructor.
- **DRY:** driver selection is delegated to strategy implementations rather than duplicated in ride-booking logic.
- **KISS / YAGNI:** the project uses in-memory collections and a console menu; it intentionally avoids databases, authentication, maps APIs, and payment integrations.
- **Law of Demeter:** services call their direct collaborators and avoid exposing nested object navigation to the console layer.

## State lifecycle

- A ride is created as `REQUESTED`.
- Once a compatible available driver is selected, it becomes `ASSIGNED`.
- Completing an assigned ride generates a fare receipt and changes its status to `COMPLETED`.
- A requested or assigned ride may be cancelled and becomes `CANCELLED`.
- Completed and cancelled rides cannot be completed again.

## Limitations

Data is stored in memory and is lost when the program exits. This is intentional for the MVP. The implementation does not include persistence, concurrent booking protection, maps/geocoding, or payment processing.

## Assignment documents

- `docs/Requirements.md`
- `docs/Class_Model.md`
- `docs/SOLID_Reflection.md`
- `docs/Object_Relationships.md`
