public class Drone extends Robot {
    public Drone(String name, String id, String location, int batteryLevel) { super(name, id, location, batteryLevel); }
    public boolean canPerform(Mission mission) { return mission.getType() == "RESCUE" && getBatteryLevel() >= 10; }
    public boolean canHandle(String type) { return type == "ROVER_BREAKDOWN"; }
    public String role() { return "Drone"; }
}
