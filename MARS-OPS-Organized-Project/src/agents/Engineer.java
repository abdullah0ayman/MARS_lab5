public class Engineer extends CrewMember {
    public Engineer(String name, String id, String location) { super(name, id, location); }
    public boolean canPerform(Mission mission) { return mission.getType() == "REPAIR" && hasSkill(mission.getRequiredSkill()); }
    public boolean canHandle(String type) { return type == "OXYGEN_LEAK" || type == "FIRE" || type == "ROVER_BREAKDOWN"; }
    public String role() { return "Engineer"; }
}
