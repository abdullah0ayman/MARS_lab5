package marsops.app;

import java.util.Arrays;
import marsops.agents.Engineer;
import marsops.agents.ExplorationRover;
import marsops.agents.Scientist;
import marsops.agents.RepairRobot;
import marsops.colony.MarsColony;
import marsops.common.EmergencyState;
import marsops.common.EnvironmentCondition;
import marsops.common.MissionState;
import marsops.common.MissionType;
import marsops.common.ResourceType;
import marsops.emergency.Emergency;
import marsops.facilities.Habitat;
import marsops.facilities.Laboratory;
import marsops.facilities.StorageFacility;
import marsops.mission.Mission;

public class ProjectTests {
    public static void main(String[] args) {
        testMissionLifecycleAndRelease();
        testOnlyOneMissionAndNoRetry();
        testAtomicResourceConsumption();
        testEquipmentFailure();
        testUnresolvedOxygenLeak();
        testActiveMissionFailsDuringDustStorm();
        System.out.println("All project tests passed.");
    }

    private static void testMissionLifecycleAndRelease() {
        MarsColony colony = new MarsColony("Test Colony");
        Scientist scientist = new Scientist("Scientist", "S1", "Habitat");
        scientist.addSkill("geology");
        ExplorationRover rover = new ExplorationRover("Rover", "R1", "Garage", 80);
        colony.addCrewMember(scientist);
        colony.addRobot(rover);

        Mission mission = new Mission("Explore", MissionType.EXPLORATION,
                2, "geology", null, null);
        colony.submitMission(mission, Arrays.asList(scientist, rover));
        check(mission.getState() == MissionState.ASSIGNED, "Mission should be assigned.");
        check(!scientist.isAvailable() && !rover.isAvailable(), "Agents should become busy.");

        colony.advanceTime(1);
        check(mission.getState() == MissionState.IN_PROGRESS, "Mission should be in progress.");
        colony.advanceTime(1);
        check(mission.getState() == MissionState.COMPLETED, "Mission should complete.");
        check(scientist.isAvailable() && rover.isAvailable(), "Agents should be released.");
    }

    private static void testOnlyOneMissionAndNoRetry() {
        MarsColony colony = new MarsColony("Test Colony");
        Scientist scientist = new Scientist("Scientist", "S1", "Habitat");
        scientist.addSkill("geology");
        ExplorationRover rover = new ExplorationRover("Rover", "R1", "Garage", 80);
        colony.addCrewMember(scientist);
        colony.addRobot(rover);

        Mission first = new Mission("First", MissionType.EXPLORATION,
                3, "geology", null, null);
        Mission second = new Mission("Second", MissionType.EXPLORATION,
                2, "geology", null, null);
        colony.submitMission(first, Arrays.asList(scientist, rover));
        colony.submitMission(second, Arrays.asList(scientist, rover));
        check(second.getState() == MissionState.FAILED, "Second mission should fail immediately.");
        check(!second.getFailureReason().isEmpty(), "Failed mission needs a reason.");

        second.startProgress();
        check(second.getState() == MissionState.FAILED, "Failed missions cannot be retried.");
    }

    private static void testAtomicResourceConsumption() {
        MarsColony colony = new MarsColony("Test Colony");
        StorageFacility first = new StorageFacility("First", "A", 10);
        StorageFacility second = new StorageFacility("Second", "B", 10);
        StorageFacility damaged = new StorageFacility("Damaged", "C", 10);
        first.getResources().add(ResourceType.BATTERY_POWER, 3);
        second.getResources().add(ResourceType.BATTERY_POWER, 2);
        damaged.getResources().add(ResourceType.BATTERY_POWER, 100);
        damaged.setOperational(false);
        colony.addFacility(first);
        colony.addFacility(second);
        colony.addFacility(damaged);

        check(colony.totalResource(ResourceType.BATTERY_POWER) == 5,
                "Only operational facilities should count.");
        Mission impossible = new Mission("Impossible", MissionType.REPAIR,
                1, "repair", null, null);
        impossible.requireResource(ResourceType.BATTERY_POWER, 6);
        check(!colony.consumeResources(impossible.getResourceRequirements()),
                "Short resource operation should fail.");
        check(first.getResources().get(ResourceType.BATTERY_POWER) == 3
                && second.getResources().get(ResourceType.BATTERY_POWER) == 2,
                "Failed consumption must not remove resources.");

        Mission possible = new Mission("Possible", MissionType.REPAIR,
                1, "repair", null, null);
        possible.requireResource(ResourceType.BATTERY_POWER, 5);
        check(colony.consumeResources(possible.getResourceRequirements()),
                "Resources should be collected from both facilities.");
        check(colony.totalResource(ResourceType.BATTERY_POWER) == 0,
                "Consumed resources should become zero.");
}

    private static void testEquipmentFailure() {
        MarsColony colony = new MarsColony("Test Colony");
        Laboratory lab = new Laboratory("Lab", "A", 5);
        Scientist scientist = new Scientist("Scientist", "S1", "Lab");
        scientist.addSkill("geology");
        colony.addFacility(lab);
        colony.addCrewMember(scientist);

        Mission mission = new Mission("Research", MissionType.RESEARCH,
                1, "geology", "Spectrometer", lab);
        colony.submitMission(mission, Arrays.asList(scientist));
        check(mission.getState() == MissionState.FAILED,
                "Missing equipment should fail the mission.");
    }

    private static void testUnresolvedOxygenLeak() {
        MarsColony colony = new MarsColony("Test Colony");
        Habitat habitat = new Habitat("Habitat", "A", 5);
        habitat.getResources().add(ResourceType.OXYGEN, 100);
        Engineer engineer = new Engineer("Engineer", "E1", "A");
        RepairRobot robot = new RepairRobot("Robot", "R1", "A", 50);
        engineer.setAvailable(false);
        robot.setAvailable(false);
        colony.addFacility(habitat);
        colony.addCrewMember(engineer);
        colony.addRobot(robot);

        Emergency emergency = colony.handleOxygenLeak(habitat, 10);
        check(emergency.getState() == EmergencyState.UNRESOLVED,
                "Unavailable responders should leave the emergency unresolved.");
        check(habitat.getResources().get(ResourceType.OXYGEN) == 80,
                "Unresolved oxygen leak should apply the loss twice.");
}

    private static void testActiveMissionFailsDuringDustStorm() {
        MarsColony colony = new MarsColony("Test Colony");
        Scientist scientist = new Scientist("Scientist", "S1", "Habitat");
        scientist.addSkill("geology");
        ExplorationRover rover = new ExplorationRover("Rover", "R1", "Garage", 80);
        colony.addCrewMember(scientist);
        colony.addRobot(rover);
        Mission mission = new Mission("Explore", MissionType.EXPLORATION,
                4, "geology", null, null);
        colony.submitMission(mission, Arrays.asList(scientist, rover));
        colony.advanceTime(1);
        colony.setEnvironment(EnvironmentCondition.DUST_STORM);
        check(mission.getState() == MissionState.FAILED,
                "Dust storm should fail an active exploration mission.");
        check(scientist.isAvailable() && rover.isAvailable(),
                "Agents should be released after environmental failure.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
