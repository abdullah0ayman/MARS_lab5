package marsops.agents;

import marsops.common.EmergencyType;
import marsops.common.MissionType;
import marsops.mission.Mission;

public class Scientist extends CrewMember {
    public Scientist(String name, String id, String location) {
        super(name, id, location);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return (mission.getType() == MissionType.EXPLORATION
                || mission.getType() == MissionType.RESEARCH)
                && hasSkill(mission.getRequiredSkill());
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.FIRE;
    }

    @Override
    public String role() {
        return "Scientist";
    }
}
