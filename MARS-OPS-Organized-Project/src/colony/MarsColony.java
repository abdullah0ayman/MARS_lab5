import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarsColony {
    private final String name;
    private final List<CrewMember> crew = new ArrayList<>();
    private final List<Robot> robots = new ArrayList<>();
    private final List<Facility> facilities = new ArrayList<>();
    private final List<Mission> missions = new ArrayList<>();
    private final List<Emergency> emergencies = new ArrayList<>();
    private final List<Agent> activeMissionAgents = new ArrayList<>();
    private Mission activeMission;
    private String environment = "NORMAL";
    private int simulationTime;

    public MarsColony(String name) { this.name = name; }
    public void addCrewMember(CrewMember person) { crew.add(person); }
    public void addRobot(Robot robot) { robots.add(robot); }
    public void addFacility(Facility facility) { facilities.add(facility); }
    public List<CrewMember> getCrew() { return crew; }
    public List<Robot> getRobots() { return robots; }
    public List<Facility> getFacilities() { return facilities; }
    public List<Mission> getMissions() { return missions; }
    public List<Emergency> getEmergencies() { return emergencies; }
    public Mission getActiveMission() { return activeMission; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String condition) {
        environment = condition;
        if (condition == "DUST_STORM" && activeMission != null
                && activeMission.getState() == "IN_PROGRESS"
                && activeMission.getType() == "EXPLORATION") {
            finishMissionWithFailure("Dust storm caused the active surface mission to fail.");
        }
    }
    public int getSimulationTime() { return simulationTime; }

    public int totalResource(String type) {
        int total = 0;
        for (Facility facility : facilities) if (facility.isOperational()) total += facility.getResources().get(type);
        return total;
    }

    public boolean consumeResources(Map<String, Integer> required) {
        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            if (totalResource(entry.getKey()) < entry.getValue()) return false;
        }
        // First operational facilities supply resources first; this deterministic policy is documented in README.
        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            int remaining = entry.getValue();
            for (Facility facility : facilities) {
                if (remaining == 0) break;
                if (!facility.isOperational()) continue;
                int amount = Math.min(remaining, facility.getResources().get(entry.getKey()));
                facility.getResources().remove(entry.getKey(), amount);
                remaining -= amount;
            }
        }
        return true;
    }

    private List<Agent> allAgents() {
        List<Agent> agents = new ArrayList<>(); agents.addAll(crew); agents.addAll(robots); return agents;
    }

    public void submitMission(Mission mission, List<Agent> agents) {
        missions.add(mission);
        if (activeMission != null && !activeMission.isFinished()) { mission.fail("Another mission is already active."); return; }
        if (mission.getType() == "EXPLORATION" && environment == "DUST_STORM") {
            mission.fail("Surface operations are not allowed during a dust storm."); return;
        }
        if (mission.getType() == "RESCUE" && environment == "COMMUNICATION_DISRUPTION") {
            mission.fail("Rescue cannot start during communication disruption."); return;
        }
        if (mission.getTargetFacility() != null && !mission.getTargetFacility().isOperational()) {
            mission.fail("Target facility is not operational."); return;
        }
        for (Agent agent : agents) {
            if (!allAgents().contains(agent) || !agent.isAvailable()) { mission.fail("An assigned agent is unavailable."); return; }
            if (!agent.isUsable() || !agent.canPerform(mission)) { mission.fail("Agent " + agent.getName() + " does not possess the required capability."); return; }
        }
        if (!consumeResources(mission.getResourceRequirements())) { mission.fail("Insufficient resources; no resources were consumed."); return; }
        activeMissionAgents.clear();
        for (Agent agent : agents) {
            agent.setAvailable(false);
            activeMissionAgents.add(agent);
            if (mission.getType() == "EXPLORATION") agent.moveTo("Martian Surface");
        }
        activeMission = mission;
        mission.setState("ASSIGNED");
    }

    public void advanceTime(int units) {
        for (int i = 0; i < units; i++) {
            simulationTime++;
            if (activeMission == null || activeMission.isFinished()) continue;
            if (activeMission.getState() == "ASSIGNED") activeMission.startProgress();
            else activeMission.advanceOneUnit();
            if (activeMission.isFinished()) releaseMissionAgents();
        }
    }

    private void finishMissionWithFailure(String reason) { if (activeMission != null) { activeMission.fail(reason); releaseMissionAgents(); } }
    private void releaseMissionAgents() {
        for (Agent agent : activeMissionAgents) agent.setAvailable(true);
        activeMissionAgents.clear();
        activeMission = null;
    }

    private List<Agent> availableResponders(String type, boolean needHuman, boolean needRobot) {
        List<Agent> result = new ArrayList<>();
        for (Agent agent : allAgents()) if (agent.isAvailable() && agent.isUsable() && agent.canHandle(type)) result.add(agent);
        boolean human = false, robot = false;
        for (Agent agent : result) { human |= agent instanceof CrewMember; robot |= agent instanceof Robot; }
        if ((needHuman && !human) || (needRobot && !robot)) return new ArrayList<>();
        return result;
    }

    private Emergency beginEmergency(String type, String location) { Emergency emergency = new Emergency(type, location); emergencies.add(emergency); emergency.setState("RESPONDING"); return emergency; }
    private void dispatch(List<Agent> responders, String location) { for (Agent responder : responders) { responder.setAvailable(false); responder.moveTo(location); } }
    private void releaseResponders(List<Agent> responders) { for (Agent responder : responders) responder.setAvailable(true); }

    public Emergency handleOxygenLeak(Habitat habitat, int oxygenLoss) {
        Emergency emergency = beginEmergency("OXYGEN_LEAK", habitat.getName());
        habitat.setOxygenLeak(true);
        habitat.getResources().remove("OXYGEN", oxygenLoss);
        List<Agent> responders = availableResponders("OXYGEN_LEAK", true, true);
        if (responders.isEmpty()) { habitat.getResources().remove("OXYGEN", oxygenLoss); emergency.setState("UNRESOLVED"); emergency.setOutcome("No suitable human and robotic responders were available; oxygen loss applied twice."); return emergency; }
        Map<String, Integer> responseCost = new HashMap<>(); responseCost.put("SPARE_PARTS", 1); responseCost.put("BATTERY_POWER", 2);
        if (!consumeResources(responseCost)) { habitat.getResources().remove("OXYGEN", oxygenLoss); emergency.setState("UNRESOLVED"); emergency.setOutcome("Response resources were insufficient; no response resources consumed and oxygen loss applied twice."); return emergency; }
        dispatch(responders, habitat.getLocation()); habitat.setOxygenLeak(false); releaseResponders(responders);
        emergency.setState("RESOLVED"); emergency.setOutcome("Leak repaired; initial oxygen loss only. Responders: " + responders.size()); return emergency;
    }

    public Emergency handleMedicalEmergency(CrewMember patient, Facility facility) {
        Emergency emergency = beginEmergency("MEDICAL_EMERGENCY", facility.getName());
        List<Agent> responders = availableResponders("MEDICAL_EMERGENCY", true, false);
        if (responders.isEmpty() || totalResource("MEDICAL_SUPPLIES") < 1 || !facility.isOperational()) { emergency.setState("UNRESOLVED"); emergency.setOutcome("Medical officer, supplies, or operational facility unavailable."); return emergency; }
        Map<String, Integer> cost = new HashMap<>(); cost.put("MEDICAL_SUPPLIES", 1); consumeResources(cost);
        dispatch(responders, facility.getLocation()); patient.setString("HEALTHY"); releaseResponders(responders); emergency.setState("RESOLVED"); emergency.setOutcome("Patient treated successfully."); return emergency;
    }

    public Emergency handleFire(Laboratory lab) {
        Emergency emergency = beginEmergency("FIRE", lab.getName());
        List<Agent> responders = availableResponders("FIRE", true, true);
        if (responders.isEmpty()) { lab.setOperational(false); emergency.setState("UNRESOLVED"); emergency.setOutcome("No suitable responders available."); return emergency; }
        dispatch(responders, lab.getLocation()); lab.setOperational(false); lab.addEquipment("Fire-damaged equipment"); releaseResponders(responders); emergency.setState("RESOLVED"); emergency.setOutcome("Fire contained; laboratory temporarily unavailable."); return emergency;
    }

    public Emergency handleRoverBreakdown(ExplorationRover rover, Mission mission) {
        Emergency emergency = beginEmergency("ROVER_BREAKDOWN", rover.getCurrentLocation());
        rover.setDamaged(true);
        if (mission != null && mission.getState() != "COMPLETED" && mission.getState() != "FAILED") {
            mission.fail("Exploration rover broke down during the mission.");
            if (activeMission == mission) releaseMissionAgents();
        }
        emergency.setState("RESOLVED");
        emergency.setOutcome("Rover marked damaged at its breakdown location; create a repair mission for recovery.");
        return emergency;
    }

    public String statusReport() {
        StringBuilder report = new StringBuilder("\n=== " + name + " STATUS (time " + simulationTime + ") ===\n");
        report.append("Environment: ").append(environment).append("\nResources: ");
        for (String type : ResourceNames.ALL) report.append(type).append("=").append(totalResource(type)).append(" ");
        report.append("\nCrew:\n"); for (CrewMember person : crew) report.append("  ").append(person).append("\n");
        report.append("Robots:\n"); for (Robot robot : robots) report.append("  ").append(robot).append("\n");
        report.append("Facilities:\n"); for (Facility facility : facilities) report.append("  ").append(facility).append("\n");
        report.append("Active mission: ").append(activeMission == null ? "none" : activeMission).append("\n");
        report.append("Emergencies: ").append(emergencies).append("\n");
        return report.toString();
    }
}
