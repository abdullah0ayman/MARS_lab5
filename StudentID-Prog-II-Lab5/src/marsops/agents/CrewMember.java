package marsops.agents;

import java.util.ArrayList;
import java.util.List;
import marsops.common.HealthStatus;

public abstract class CrewMember extends Agent {
    private HealthStatus healthStatus = HealthStatus.HEALTHY;
    private int fatigue;
    private final List<String> skills = new ArrayList<>();

    protected CrewMember(String name, String id, String location) {
        super(name, id, location);
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public int getFatigue() {
        return fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = Math.max(0, Math.min(100, fatigue));
    }

    public List<String> getSkills() {
        return skills;
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.isBlank()) {
            skills.add(skill);
        }
    }

    public boolean hasSkill(String skill) {
        return skill == null || skills.contains(skill);
    }

    @Override
    public String toString() {
        return role() + " " + getName() + " (available=" + isAvailable()
                + ", location=" + getCurrentLocation() + ")";
    }
}
