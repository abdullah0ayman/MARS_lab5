public abstract class Robot extends Agent {
    private int batteryLevel;
    protected Robot(String name, String id, String location, int batteryLevel) { super(name, id, location); this.batteryLevel = batteryLevel; }
    public int getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(int batteryLevel) { this.batteryLevel = Math.max(0, batteryLevel); }
    public void useBattery(int amount) { setBatteryLevel(batteryLevel - amount); }
    public String toString() { return role() + " " + getName() + " (battery=" + batteryLevel + ", available=" + isAvailable() + ")"; }
}
