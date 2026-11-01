package marsops.app;

import java.util.Arrays;
import marsops.agents.Engineer;
import marsops.agents.ExplorationRover;
import marsops.agents.MedicalOfficer;
import marsops.agents.Scientist;
import marsops.agents.RepairRobot;
import marsops.colony.MarsColony;
import marsops.common.EnvironmentCondition;
import marsops.common.MissionType;
import marsops.common.ResourceType;
import marsops.emergency.Emergency;
import marsops.facilities.Habitat;
import marsops.facilities.Laboratory;
import marsops.facilities.StorageFacility;
import marsops.mission.Mission;

public class Main {
    public static void main(String[] args) {
        MarsColony colony = buildColony();
        successfulExploration(colony);
        missionRejection(colony);
        resourceShortage(colony);
        emergencyHandling(colony);
        environmentalEffect(colony);
    }

    private static MarsColony buildColony() {
        MarsColony colony = new MarsColony("Ares Research Colony");

        Habitat habitat = new Habitat("Habitat A", "Sector A", 20);
        habitat.getResources().add(ResourceType.OXYGEN, 300);
        habitat.getResources().add(ResourceType.WATER, 100);

        StorageFacility storage = new StorageFacility("Central Storage", "Sector B", 100);
        storage.getResources().add(ResourceType.OXYGEN, 200);
        storage.getResources().add(ResourceType.SPARE_PARTS, 10);
        storage.getResources().add(ResourceType.BATTERY_POWER, 20);
        storage.getResources().add(ResourceType.MEDICAL_SUPPLIES, 5);

        Laboratory lab = new Laboratory("Mars Lab", "Sector C", 8);
        lab.addEquipment("Spectrometer");

        colony.addFacility(habitat);
        colony.addFacility(storage);
        colony.addFacility(lab);

        Scientist scientist = new Scientist("Dr. Lina", "C-001", "Habitat A");
        scientist.addSkill("geology");
        Engineer engineer = new Engineer("Eng. Omar", "C-002", "Habitat A");
        engineer.addSkill("repair");
        MedicalOfficer medic = new MedicalOfficer("Nurse Sara", "C-003", "Habitat A");

        colony.addCrewMember(scientist);
        colony.addCrewMember(engineer);
        colony.addCrewMember(medic);
        colony.addRobot(new ExplorationRover("Rover-1", "R-001", "Garage", 80));
        colony.addRobot(new RepairRobot("RepairBot-1", "R-002", "Garage", 60));

        return colony;
    }

    private static void successfulExploration(MarsColony colony) {
        System.out.println("\n--- Scenario 1: Successful Exploration ---");
        System.out.println(colony.statusReport());

        Scientist scientist = (Scientist) colony.getCrew().get(0);
        ExplorationRover rover = (ExplorationRover) colony.getRobots().get(0);
        Mission mission = new Mission("Explore Jezero Crater", MissionType.EXPLORATION,
                3, "geology", null, null);
        mission.requireResource(ResourceType.BATTERY_POWER, 5);

        colony.submitMission(mission, Arrays.asList(scientist, rover));
        colony.advanceTime(3);
        System.out.println("After execution: " + mission);
        System.out.println(colony.statusReport());
    }

    private static void missionRejection(MarsColony colony) {
        System.out.println("\n--- Scenario 2: Mission Rejection ---");
        MedicalOfficer medic = (MedicalOfficer) colony.getCrew().get(2);
        ExplorationRover rover = (ExplorationRover) colony.getRobots().get(0);
        Mission mission = new Mission("Invalid Exploration", MissionType.EXPLORATION,
                2, "geology", null, null);

        colony.submitMission(mission, Arrays.asList(medic, rover));
        System.out.println(mission + " (active mission=" + colony.getActiveMission() + ")");
    }

    private static void resourceShortage(MarsColony colony) {
        System.out.println("\n--- Scenario 3: Resource Shortage ---");
        int before = colony.totalResource(ResourceType.FUEL);
        Engineer engineer = (Engineer) colony.getCrew().get(1);
        RepairRobot robot = (RepairRobot) colony.getRobots().get(1);
        Mission mission = new Mission("Fuel-Hungry Repair", MissionType.REPAIR,
                2, "repair", null, colony.getFacilities().get(1));
        mission.requireResource(ResourceType.FUEL, 999);

        colony.submitMission(mission, Arrays.asList(engineer, robot));
        System.out.println("Fuel before=" + before + ", after="
                + colony.totalResource(ResourceType.FUEL));
        System.out.println(mission);
    }

    private static void emergencyHandling(MarsColony colony) {
        System.out.println("\n--- Scenario 4: Oxygen Leak Emergency ---");
        Habitat habitat = (Habitat) colony.getFacilities().get(0);
        int before = habitat.getResources().get(ResourceType.OXYGEN);
        Emergency emergency = colony.handleOxygenLeak(habitat, 25);

        System.out.println("Habitat oxygen before=" + before + ", after="
                + habitat.getResources().get(ResourceType.OXYGEN));
        System.out.println(emergency);
    }

    private static void environmentalEffect(MarsColony colony) {
        System.out.println("\n--- Scenario 5: Environmental Effect ---");
        colony.setEnvironment(EnvironmentCondition.DUST_STORM);
        Scientist scientist = (Scientist) colony.getCrew().get(0);
        ExplorationRover rover = (ExplorationRover) colony.getRobots().get(0);
        Mission mission = new Mission("Storm Exploration", MissionType.EXPLORATION,
                2, "geology", null, null);

        colony.submitMission(mission, Arrays.asList(scientist, rover));
        System.out.println(mission);
    }
}
