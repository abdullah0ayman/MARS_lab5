package marsops.agents;

import marsops.common.EmergencyType;
import marsops.common.MissionType;
import marsops.mission.Mission;

public class ExplorationRover extends Robot {
    public ExplorationRover(String name, String id, String location, int batteryLevel) {
        super(name, id, location, batteryLevel);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return mission.getType() == MissionType.EXPLORATION
                && getBatteryLevel() >= 10;
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.ROVER_BREAKDOWN;
    }

    @Override
    public String role() {
        return "Exploration Rover";
    }
}
