# MARS-OPS Organized Simple Team Project

This is the organized MARS-OPS team project using a simple beginner-level design. It does **not** use Java enums. Mission types, mission states, emergency types, facility types, environmental conditions, health statuses, and resource names are represented with strings.

## Folder structure

```text
MARS-OPS-Organized-Simple/
├── README.md
├── docs/
│   └── PROJECT-STRUCTURE.md
└── src/
    ├── common/
    │   ├── ResourceNames.java
    │   ├── ResourceInventory.java
    │   └── LifeSupportSystem.java
    ├── mission/
    │   └── Mission.java
    ├── agents/
    │   ├── Agent.java
    │   ├── CrewMember.java
    │   ├── Scientist.java
    │   ├── Engineer.java
    │   ├── MedicalOfficer.java
    │   ├── Robot.java
    │   ├── ExplorationRover.java
    │   ├── RepairRobot.java
    │   └── Drone.java
    ├── facilities/
    │   ├── Facility.java
    │   ├── Habitat.java
    │   ├── Laboratory.java
    │   ├── StorageFacility.java
    │   ├── PowerStation.java
    │   └── CommunicationStation.java
    ├── emergency/
    │   └── Emergency.java
    ├── colony/
    │   └── MarsColony.java
    └── app/
        └── Main.java
```

## No-enum rule

The project uses strings such as `"EXPLORATION"`, `"ASSIGNED"`, `"DUST_STORM"`, and `"OXYGEN"`. This keeps the code close to the simple design agreed for the team. `ResourceNames.ALL` is a normal string array used when the status report needs to visit every resource.

## Compile and run

From the project root:

```bash
rm -rf out
mkdir out
javac -Xlint:all -d out $(find src -name '*.java')
java -cp out Main
```

The source files have no package declarations, which keeps compilation simple while the folders show each class responsibility.
