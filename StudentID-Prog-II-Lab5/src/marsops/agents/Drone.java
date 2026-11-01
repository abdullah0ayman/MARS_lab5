package marsops.agents;

import marsops.common.EmergencyType;
import marsops.common.MissionType;
import marsops.mission.Mission;

public class Drone extends Robot {
    public Drone(String name, String id, String location, int batteryLevel) {
        super(name, id, location, batteryLevel);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return mission.getType() == MissionType.RESCUE
                && getBatteryLevel() >= 10;
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.ROVER_BREAKDOWN;
    }

    @Override
    public String role() {
        return "Drone";
    }
}
