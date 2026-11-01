package marsops.colony;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import marsops.agents.Agent;
import marsops.agents.CrewMember;
import marsops.agents.Engineer;
import marsops.agents.ExplorationRover;
import marsops.agents.Robot;
import marsops.common.EmergencyState;
import marsops.common.EmergencyType;
import marsops.common.EnvironmentCondition;
import marsops.common.HealthStatus;
import marsops.common.MissionState;
import marsops.common.MissionType;
import marsops.common.ResourceType;
import marsops.emergency.Emergency;
import marsops.facilities.Facility;
import marsops.facilities.Habitat;
import marsops.facilities.Laboratory;
import marsops.mission.Mission;

public class MarsColony {
    private final String name;
    private final List<CrewMember> crew = new ArrayList<>();
    private final List<Robot> robots = new ArrayList<>();
    private final List<Facility> facilities = new ArrayList<>();
    private final List<Mission> missions = new ArrayList<>();
    private final List<Emergency> emergencies = new ArrayList<>();
    private final List<Agent> activeMissionAgents = new ArrayList<>();
    private Mission activeMission;
    private EnvironmentCondition environment = EnvironmentCondition.NORMAL;
    private int simulationTime;

    public MarsColony(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Colony name is required.");
        }
        this.name = name;
    }

    public void addCrewMember(CrewMember person) {
        if (person != null) {
            crew.add(person);
        }
    }

    public void addRobot(Robot robot) {
        if (robot != null) {
            robots.add(robot);
        }
    }

    public void addFacility(Facility facility) {
        if (facility != null) {
            facilities.add(facility);
        }
    }

    public List<CrewMember> getCrew() {
        return crew;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public List<Emergency> getEmergencies() {
        return emergencies;
    }

    public Mission getActiveMission() {
        return activeMission;
    }

    public EnvironmentCondition getEnvironment() {
        return environment;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public void setEnvironment(EnvironmentCondition condition) {
        if (condition == null) {
            return;
        }
        environment = condition;
        if (activeMission != null && activeMission.getState() == MissionState.IN_PROGRESS) {
            if (condition == EnvironmentCondition.DUST_STORM
                    && activeMission.getType() == MissionType.EXPLORATION) {
                finishMissionWithFailure("Dust storm caused the active surface mission to fail.");
            } else if (condition == EnvironmentCondition.COMMUNICATION_DISRUPTION
                    && activeMission.getType() == MissionType.RESCUE) {
                finishMissionWithFailure("Communication disruption stopped the rescue mission.");
            }
        }
    }

    public int totalResource(ResourceType type) {
        int total = 0;
        for (Facility facility : facilities) {
            if (facility.isOperational()) {
                total += facility.getResources().get(type);
            }
        }
        return total;
    }

    public boolean consumeResources(Map<ResourceType, Integer> required) {
        for (Map.Entry<ResourceType, Integer> entry : required.entrySet()) {
            if (entry.getValue() < 0 || totalResource(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        for (Map.Entry<ResourceType, Integer> entry : required.entrySet()) {
            int remaining = entry.getValue();
            for (Facility facility : facilities) {
                if (remaining == 0) {
                    break;
                }
                if (!facility.isOperational()) {
                    continue;
                }
                int available = facility.getResources().get(entry.getKey());
                int amount = Math.min(remaining, available);
                if (facility.getResources().remove(entry.getKey(), amount)) {
                    remaining -= amount;
                }
            }
        }
        return true;
    }

    public void submitMission(Mission mission, List<Agent> agents) {
        if (mission == null) {
            return;
        }
        missions.add(mission);

        if (activeMission != null && !activeMission.isFinished()) {
            mission.fail("Another mission is already active.");
            return;
        }
        if (agents == null || agents.isEmpty()) {
            mission.fail("At least one agent is required.");
            return;
        }
        if (new HashSet<>(agents).size() != agents.size()) {
            mission.fail("The same agent cannot be assigned twice.");
            return;
        }
        if (mission.getType() == MissionType.EXPLORATION
                && environment == EnvironmentCondition.DUST_STORM) {
            mission.fail("Surface operations are not allowed during a dust storm.");
            return;
        }
        if (mission.getType() == MissionType.RESCUE
                && environment == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
            mission.fail("Rescue cannot start during communication disruption.");
            return;
        }
        if (mission.getTargetFacility() != null
                && !mission.getTargetFacility().isOperational()) {
            mission.fail("Target facility is not operational.");
            return;
        }
        if (mission.getTargetFacility() != null
                && !mission.getTargetFacility().hasEquipment(mission.getRequiredEquipment())) {
            mission.fail("Required equipment is not available at the target facility.");
            return;
        }

        for (Agent agent : agents) {
            if (!isColonyAgent(agent) || !agent.isAvailable()) {
                mission.fail("An assigned agent is unavailable.");
                return;
            }
            if (!agent.isUsable() || !agent.canPerform(mission)) {
                mission.fail("Agent " + agent.getName()
                        + " does not possess the required capability.");
                return;
            }
        }

        if (!consumeResources(mission.getResourceRequirements())) {
            mission.fail("Insufficient resources; no resources were consumed.");
            return;
        }

        for (Agent agent : agents) {
            agent.setAvailable(false);
            activeMissionAgents.add(agent);
            if (mission.getType() == MissionType.EXPLORATION) {
                agent.moveTo("Martian Surface");
            }
        }
        activeMission = mission;
    }

    public void advanceTime(int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Time cannot move backwards.");
        }
        for (int i = 0; i < units; i++) {
            simulationTime++;
            if (activeMission == null || activeMission.isFinished()) {
                continue;
            }
            if (activeMission.getState() == MissionState.ASSIGNED) {
                activeMission.startProgress();
            }
            activeMission.advanceOneUnit();
            if (activeMission.isFinished()) {
                releaseMissionAgents();
            }
        }
    }

    private boolean isColonyAgent(Agent agent) {
        return crew.contains(agent) || robots.contains(agent);
    }

    private void finishMissionWithFailure(String reason) {
        if (activeMission != null) {
            activeMission.fail(reason);
            releaseMissionAgents();
        }
    }

    private void releaseMissionAgents() {
        for (Agent agent : activeMissionAgents) {
            agent.setAvailable(true);
        }
        activeMissionAgents.clear();
        activeMission = null;
    }

    private List<Agent> allAgents() {
        List<Agent> agents = new ArrayList<>();
        agents.addAll(crew);
        agents.addAll(robots);
        return agents;
    }

    private List<Agent> availableResponders(EmergencyType type,
                                            boolean needHuman,
                                            boolean needRobot) {
        List<Agent> result = new ArrayList<>();
        boolean humanFound = false;
        boolean robotFound = false;

        for (Agent agent : allAgents()) {
            if (agent.isAvailable() && agent.isUsable() && agent.canHandle(type)) {
                result.add(agent);
                humanFound = humanFound || agent instanceof CrewMember;
                robotFound = robotFound || agent instanceof Robot;
            }
        }

        if ((needHuman && !humanFound) || (needRobot && !robotFound)) {
            return new ArrayList<>();
        }
        return result;
    }

    private Emergency beginEmergency(EmergencyType type, String location) {
        Emergency emergency = new Emergency(type, location);
        emergencies.add(emergency);
        emergency.setState(EmergencyState.RESPONDING);
        return emergency;
    }

    private void dispatch(List<Agent> responders, String location) {
        for (Agent responder : responders) {
            responder.setAvailable(false);
            responder.moveTo(location);
        }
    }

    private void releaseResponders(List<Agent> responders) {
        for (Agent responder : responders) {
            responder.setAvailable(true);
        }
    }

    public Emergency handleOxygenLeak(Habitat habitat, int oxygenLoss) {
        Emergency emergency = beginEmergency(EmergencyType.OXYGEN_LEAK, habitat.getName());
        habitat.setOxygenLeak(true);
        habitat.getResources().remove(ResourceType.OXYGEN, oxygenLoss);

        List<Agent> responders = availableResponders(EmergencyType.OXYGEN_LEAK, true, true);
        if (responders.isEmpty()) {
            habitat.getResources().remove(ResourceType.OXYGEN, oxygenLoss);
            habitat.setOxygenLeak(true);
            emergency.setState(EmergencyState.UNRESOLVED);
            emergency.setOutcome("No suitable human and robotic responders were available; oxygen loss applied twice.");
            return emergency;
        }

        Map<ResourceType, Integer> responseCost = new EnumMap<>(ResourceType.class);
        responseCost.put(ResourceType.SPARE_PARTS, 1);
        responseCost.put(ResourceType.BATTERY_POWER, 2);
        if (!consumeResources(responseCost)) {
            habitat.getResources().remove(ResourceType.OXYGEN, oxygenLoss);
            emergency.setState(EmergencyState.UNRESOLVED);
            emergency.setOutcome("Response resources were insufficient; no response resources consumed and oxygen loss applied twice.");
            return emergency;
        }

        dispatch(responders, habitat.getLocation());
        habitat.setOxygenLeak(false);
        releaseResponders(responders);
        emergency.setState(EmergencyState.RESOLVED);
        emergency.setOutcome("Leak repaired; initial oxygen loss only. Responders: " + responders.size());
        return emergency;
    }

    public Emergency handleMedicalEmergency(CrewMember patient, Facility facility) {
        Emergency emergency = beginEmergency(EmergencyType.MEDICAL_EMERGENCY, facility.getName());
        List<Agent> responders = availableResponders(EmergencyType.MEDICAL_EMERGENCY, true, false);
        if (responders.isEmpty() || totalResource(ResourceType.MEDICAL_SUPPLIES) < 1
                || !facility.isOperational()) {
            emergency.setState(EmergencyState.UNRESOLVED);
            emergency.setOutcome("Medical officer, supplies, or operational facility unavailable.");
            return emergency;
        }

        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.MEDICAL_SUPPLIES, 1);
        consumeResources(cost);
        dispatch(responders, facility.getLocation());
        patient.setHealthStatus(HealthStatus.HEALTHY);
        releaseResponders(responders);
        emergency.setState(EmergencyState.RESOLVED);
        emergency.setOutcome("Patient treated successfully.");
        return emergency;
    }

    public Emergency handleFire(Laboratory lab) {
        Emergency emergency = beginEmergency(EmergencyType.FIRE, lab.getName());
        List<Agent> responders = availableResponders(EmergencyType.FIRE, true, true);
        if (responders.isEmpty()) {
            lab.setOperational(false);
            emergency.setState(EmergencyState.UNRESOLVED);
            emergency.setOutcome("No suitable responders available.");
            return emergency;
        }

        dispatch(responders, lab.getLocation());
        lab.setOperational(false);
        lab.addEquipment("Fire-damaged equipment");
        releaseResponders(responders);
        emergency.setState(EmergencyState.RESOLVED);
        emergency.setOutcome("Fire contained; laboratory temporarily unavailable.");
        return emergency;
    }

    public Emergency handleRoverBreakdown(ExplorationRover rover, Mission mission) {
        Emergency emergency = beginEmergency(EmergencyType.ROVER_BREAKDOWN,
                rover.getCurrentLocation());
        rover.setDamaged(true);
        if (mission != null && !mission.isFinished()) {
            mission.fail("Exploration rover broke down during the mission.");
            if (activeMission == mission) {
                releaseMissionAgents();
            }
        }
        List<Agent> responders = availableResponders(
                EmergencyType.ROVER_BREAKDOWN, false, true);
        if (responders.isEmpty()) {
            emergency.setState(EmergencyState.UNRESOLVED);
            emergency.setOutcome("No available repair responder could reach the rover.");
            return emergency;
        }
        dispatch(responders, rover.getCurrentLocation());
        releaseResponders(responders);
        emergency.setState(EmergencyState.RESOLVED);
        emergency.setOutcome("Rover marked damaged; repair responder dispatched. Create a repair mission for recovery.");
        return emergency;
    }

    public String statusReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n=== ").append(name).append(" STATUS (time ")
                .append(simulationTime).append(") ===\n");
        report.append("Environment: ").append(environment).append("\nResources: ");
        for (ResourceType type : ResourceType.values()) {
            report.append(type).append("=").append(totalResource(type)).append(" ");
        }
        report.append("\nCrew:\n");
        for (CrewMember person : crew) {
            report.append("  ").append(person).append("\n");
        }
        report.append("Robots:\n");
        for (Robot robot : robots) {
            report.append("  ").append(robot).append("\n");
        }
        report.append("Facilities:\n");
        for (Facility facility : facilities) {
            report.append("  ").append(facility).append("\n");
        }
        report.append("Active mission: ")
                .append(activeMission == null ? "none" : activeMission).append("\n");
        report.append("Emergencies: ").append(emergencies).append("\n");
        return report.toString();
    }
}
