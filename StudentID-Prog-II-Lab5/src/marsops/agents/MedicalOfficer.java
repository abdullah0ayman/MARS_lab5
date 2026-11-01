package marsops.agents;

import marsops.common.EmergencyType;
import marsops.common.MissionType;
import marsops.mission.Mission;

public class MedicalOfficer extends CrewMember {
    public MedicalOfficer(String name, String id, String location) {
        super(name, id, location);
    }

    @Override
    public boolean canPerform(Mission mission) {
        return mission.getType() == MissionType.RESCUE;
    }

    @Override
    public boolean canHandle(EmergencyType type) {
        return type == EmergencyType.MEDICAL_EMERGENCY
                || type == EmergencyType.FIRE;
    }

    @Override
    public String role() {
        return "Medical Officer";
    }
}
