package marsops.agents;

import marsops.common.EmergencyType;
import marsops.common.MissionType;
import marsops.mission.Mission;

public class RepairRobot extends Robot {
    public RepairRobot(String name, String id, String location, int batteryLevel) {
        super(name, id, location, batteryLevel);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return mission.getType() == MissionType.REPAIR
                && getBatteryLevel() >= 5;
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.OXYGEN_LEAK
                || type == EmergencyType.FIRE
                || type == EmergencyType.ROVER_BREAKDOWN;
    }

    @Override
    public String role() {
        return "Repair Robot";
    }
}
