public class Scientist extends CrewMember {
    public Scientist(String name, String id, String location) { super(name, id, location); }
    public boolean canPerform(Mission mission) {
        return (mission.getType() == "EXPLORATION" || mission.getType() == "RESEARCH")
                && hasSkill(mission.getRequiredSkill());
    }
    public boolean canHandle(String type) { return type == "FIRE"; }
    public String role() { return "Scientist"; }
}
