# Project Structure Guide

## `src/marsops/common`

This package contains the enums used by the project. It also contains `ResourceInventory` and `LifeSupportSystem`.

`ResourceInventory` uses an `EnumMap<ResourceType, Integer>`. It checks amounts before changing them, so a resource cannot become negative.

## `src/marsops/mission`

`Mission` stores the mission name, type, duration, required skill, required equipment, target facility, resource requirements, state, progress, and failure reason.

## `src/marsops/agents`

`Agent` is the abstract parent for all people and robots. It stores the common name, ID, location, availability, and damage status.

`CrewMember` adds health, fatigue, and skills. `Scientist`, `Engineer`, and `MedicalOfficer` implement their own mission and emergency capabilities.

`Robot` adds battery level. `ExplorationRover`, `RepairRobot`, and `Drone` implement their own capabilities.

## `src/marsops/facilities`

`Facility` stores the common name, type, location, capacity, operation status, equipment, and resources. `Habitat`, `Laboratory`, `StorageFacility`, `PowerStation`, and `CommunicationStation` inherit from it.

A `Habitat` contains a `LifeSupportSystem`. A facility contains its own `ResourceInventory`.

## `src/marsops/emergency`

`Emergency` stores the emergency type, location, state, and outcome text.

## `src/marsops/colony`

`MarsColony` coordinates the complete simulation. It submits missions, checks agents and resources, advances time, applies environmental restrictions, handles emergencies, and creates status reports.

## `src/marsops/app`

`Main` runs the five required demonstration scenarios. `ProjectTests` checks important success and failure conditions using simple test methods.

## Packages and NetBeans

The package names match the folder names. For example:

```java
package marsops.agents;
```

is used in files inside `src/marsops/agents`.

The project uses Java 17 syntax only where it is useful and has no external libraries.
