public class MedicalOfficer extends CrewMember {
    public MedicalOfficer(String name, String id, String location) { super(name, id, location); }
    public boolean canPerform(Mission mission) { return mission.getType() == "RESCUE"; }
    public boolean canHandle(String type) { return type == "MEDICAL_EMERGENCY" || type == "FIRE"; }
    public String role() { return "Medical Officer"; }
}
