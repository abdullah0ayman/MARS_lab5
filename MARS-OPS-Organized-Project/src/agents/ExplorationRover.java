public class ExplorationRover extends Robot {
    public ExplorationRover(String name, String id, String location, int batteryLevel) { super(name, id, location, batteryLevel); }
    public boolean canPerform(Mission mission) { return mission.getType() == "EXPLORATION" && getBatteryLevel() >= 10; }
    public boolean canHandle(String type) { return type == "ROVER_BREAKDOWN"; }
    public String role() { return "Exploration Rover"; }
}
