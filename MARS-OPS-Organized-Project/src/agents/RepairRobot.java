public class RepairRobot extends Robot {
    public RepairRobot(String name, String id, String location, int batteryLevel) { super(name, id, location, batteryLevel); }
    public boolean canPerform(Mission mission) { return mission.getType() == "REPAIR" && getBatteryLevel() >= 5; }
    public boolean canHandle(String type) { return type == "OXYGEN_LEAK" || type == "FIRE" || type == "ROVER_BREAKDOWN"; }
    public String role() { return "Repair Robot"; }
}
