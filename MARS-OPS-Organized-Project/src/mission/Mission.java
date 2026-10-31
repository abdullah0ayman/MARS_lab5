import java.util.HashMap;
import java.util.Map;

public class Mission {
    private final String name;
    private final String type;
    private final int duration;
    private final String requiredSkill;
    private final String requiredEquipment;
    private final Facility targetFacility;
    private final Map<String, Integer> resourceRequirements = new HashMap<>();
    private String state = "ASSIGNED";
    private int elapsedTime;
    private String failureReason = "";

    public Mission(String name, String type, int duration, String requiredSkill,
                   String requiredEquipment, Facility targetFacility) {
        this.name = name; this.type = type; this.duration = duration;
        this.requiredSkill = requiredSkill; this.requiredEquipment = requiredEquipment;
        this.targetFacility = targetFacility;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getDuration() { return duration; }
    public String getRequiredSkill() { return requiredSkill; }
    public String getRequiredEquipment() { return requiredEquipment; }
    public Facility getTargetFacility() { return targetFacility; }
    public String getState() { return state; }
    public int getElapsedTime() { return elapsedTime; }
    public String getFailureReason() { return failureReason; }
    public Map<String, Integer> getResourceRequirements() { return resourceRequirements; }
    public void requireResource(String type, int amount) { resourceRequirements.put(type, amount); }
    public void setState(String state) { this.state = state; }
    public void fail(String reason) { state = "FAILED"; failureReason = reason; }
    public void startProgress() { if (state == "ASSIGNED") state = "IN_PROGRESS"; }
    public void advanceOneUnit() {
        if (state == "IN_PROGRESS") {
            elapsedTime++;
            if (elapsedTime >= duration) state = "COMPLETED";
        }
    }
    public boolean isFinished() { return state == "COMPLETED" || state == "FAILED"; }
    public String toString() {
        return name + " [" + type + ", state=" + state + ", progress=" + elapsedTime + "/" + duration +
                (failureReason.isEmpty() ? "" : ", reason=" + failureReason) + "]";
    }
}
