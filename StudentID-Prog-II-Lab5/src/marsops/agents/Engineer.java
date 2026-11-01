package marsops.agents;

import marsops.common.EmergencyType;
import marsops.common.MissionType;
import marsops.mission.Mission;

public class Engineer extends CrewMember {
    public Engineer(String name, String id, String location) {
        super(name, id, location);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return mission.getType() == MissionType.REPAIR
                && hasSkill(mission.getRequiredSkill());
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.OXYGEN_LEAK
                || type == EmergencyType.FIRE
                || type == EmergencyType.ROVER_BREAKDOWN;
    }

    @Override
    public String role() {
        return "Engineer";
    }
}
