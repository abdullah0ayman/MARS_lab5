# MARS-OPS Programming II Lab 5

## About the project

This project simulates a small Mars colony. The colony has crew members, robots, facilities, resources, missions, and emergencies.

The project is written as a normal Java Programming II project. I used classes, inheritance, abstract classes, interfaces by method contracts, collections, and enums. The code uses simple `if` statements, loops, and methods.

## Project structure

```text
StudentID-Prog-II-Lab5/
├── README.md
├── build.xml
├── nbproject/
├── src/marsops/
│   ├── app/
│   │   ├── Main.java
│   │   └── ProjectTests.java
│   ├── agents/
│   ├── colony/
│   ├── common/
│   ├── emergency/
│   ├── facilities/
│   └── mission/
├── uml/
└── docs/
```

## Important classes

- `MarsColony` is the main coordinator. It stores the agents, facilities, missions, and emergencies.
- `Mission` stores mission information, resources, progress, state, and a failure reason.
- `Agent` is an abstract parent for people and robots.
- `CrewMember` is an abstract parent for scientists, engineers, and medical officers.
- `Robot` is an abstract parent for rovers, repair robots, and drones.
- `Facility` is the parent for habitats, laboratories, storage, power, and communication facilities.
- `ResourceInventory` stores resource quantities and prevents negative amounts.
- `Emergency` stores the emergency type, location, state, and result.

## Enums

Enums are used for values that have a fixed list of choices:

- `MissionState`: `ASSIGNED`, `IN_PROGRESS`, `COMPLETED`, `FAILED`.
- `MissionType`: `EXPLORATION`, `REPAIR`, `RESEARCH`, `RESCUE`.
- `EmergencyType`: `OXYGEN_LEAK`, `FIRE`, `ROVER_BREAKDOWN`, `MEDICAL_EMERGENCY`.
- `EmergencyState`: `DETECTED`, `RESPONDING`, `RESOLVED`, `UNRESOLVED`.
- `EnvironmentCondition`: `NORMAL`, `DUST_STORM`, `COMMUNICATION_DISRUPTION`.
- `FacilityType`: `HABITAT`, `LABORATORY`, `STORAGE`, `POWER_STATION`, `COMMUNICATION_STATION`.
- `HealthStatus`: `HEALTHY`, `INJURED`, `CRITICAL`.
- `ResourceType`: `OXYGEN`, `WATER`, `FOOD`, `FUEL`, `BATTERY_POWER`, `MEDICAL_SUPPLIES`, `SPARE_PARTS`.

Enums make comparisons safer than comparing arbitrary strings.

## Four OOP concepts

### Encapsulation

Most fields are private. For example, `ResourceInventory` controls how resources are added and removed. Other classes use public methods such as `get`, `add`, and `remove`.

### Inheritance

`Scientist`, `Engineer`, and `MedicalOfficer` inherit from `CrewMember`. `ExplorationRover`, `RepairRobot`, and `Drone` inherit from `Robot`. Facility classes inherit from `Facility`.

### Polymorphism

The colony stores different people in `List<CrewMember>` and different robots in `List<Robot>`. It calls `canPerform` and `canHandle` through the `Agent` type, while each child class gives its own result.

### Abstraction

`Agent`, `CrewMember`, and `Robot` are abstract classes. They contain common data and method contracts, but they are not created directly.

## UML relationships

- **Inheritance:** child classes extend `Agent`, `CrewMember`, `Robot`, or `Facility`.
- **Association:** agents can perform missions and the colony coordinates missions.
- **Aggregation:** `MarsColony` stores lists of crew, robots, facilities, missions, and emergencies.
- **Composition:** a `Habitat` creates its own `LifeSupportSystem`, and a `Facility` creates its own `ResourceInventory`.

Editable Mermaid files and PNG diagrams are in the `uml` folder.

## Mission rules

Only one mission can be active at one time. A mission fails immediately when an agent is unavailable, damaged, unsuitable, when resources are insufficient, or when an environmental condition blocks it.

A successful mission starts in `ASSIGNED`, changes to `IN_PROGRESS` when time advances, and changes to `COMPLETED` after its duration. A failed mission is terminal and cannot be retried. Completed and failed missions release their agents.

## Resource rules

Only operational facilities are counted. The colony can collect resources from more than one operational facility. Resource consumption is checked first and then removed, so a failed operation does not consume anything. Resource quantities cannot become negative.

## Emergency rules

An oxygen leak removes the first oxygen loss immediately. If suitable responders or response resources are not available, the same oxygen loss is applied again and the emergency is unresolved. If the response succeeds, the leak is repaired and response resources are consumed.

Emergencies only use available and usable responders. Busy agents cannot respond.

## Running with standard Java commands

Java 17 or newer is recommended.

From the project folder:

```bash
rm -rf build
mkdir build
javac -Xlint:all -d build $(find src -name '*.java')
java -cp build marsops.app.Main
java -cp build marsops.app.ProjectTests
```

The test program prints `All project tests passed.` when the important checks succeed.

## Running in NetBeans

1. Open NetBeans.
2. Select **File > Open Project**.
3. Select the `StudentID-Prog-II-Lab5` folder.
4. The main class is `marsops.app.Main`.
5. Run the project to see the five scenarios.
6. Run `marsops.app.ProjectTests` to run the extra checks.

The source files use the `marsops` package structure shown in the folders. No external libraries are needed.

## Five demonstrations

`Main` runs these scenarios:

1. Successful exploration.
2. Mission rejection because the agent is unsuitable.
3. Resource shortage with no resource consumption.
4. Oxygen leak emergency.
5. Dust-storm restriction.

The editable UML source files are included so they can be changed in NetBeans or another text editor.
