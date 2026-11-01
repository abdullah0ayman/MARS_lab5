package marsops.agents;

import marsops.common.EmergencyType;
import marsops.mission.Mission;

public abstract class Agent {
    private final String name;
    private final String id;
    private String currentLocation;
    private boolean available = true;
    private boolean damaged;

    protected Agent(String name, String id, String currentLocation) {
        this.name = name;
        this.id = id;
        this.currentLocation = currentLocation;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void moveTo(String location) {
        currentLocation = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public boolean isUsable() {
        return !damaged;
    }

    public abstract boolean canPerform(Mission mission);

    public abstract boolean canHandle(EmergencyType type);

    public abstract String role();
}
