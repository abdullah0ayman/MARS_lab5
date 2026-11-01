package marsops.agents;

import marsops.mission.Mission;

public abstract class Robot extends Agent {
    private int batteryLevel;

    protected Robot(String name, String id, String location, int batteryLevel) {
        super(name, id, location);
        setBatteryLevel(batteryLevel);
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = Math.max(0, Math.min(100, batteryLevel));
    }

    public void useBattery(int amount) {
        if (amount >= 0) {
            setBatteryLevel(batteryLevel - amount);
        }
    }

    @Override
    public String toString() {
        return role() + " " + getName() + " (battery=" + batteryLevel
                + ", available=" + isAvailable() + ")";
    }

    @Override
    public abstract boolean canPerform(Mission mission);
}
