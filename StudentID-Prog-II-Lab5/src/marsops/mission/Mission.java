package marsops.mission;

import java.util.EnumMap;
import java.util.Map;
import marsops.common.MissionState;
import marsops.common.MissionType;
import marsops.common.ResourceType;
import marsops.facilities.Facility;

public class Mission {
    private final String name;
    private final MissionType type;
    private final int duration;
    private final String requiredSkill;
    private final String requiredEquipment;
    private final Facility targetFacility;
    private final Map<ResourceType, Integer> resourceRequirements =
            new EnumMap<>(ResourceType.class);
    private MissionState state = MissionState.ASSIGNED;
    private int elapsedTime;
    private String failureReason = "";

    public Mission(String name, MissionType type, int duration,
                   String requiredSkill, String requiredEquipment,
                   Facility targetFacility) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Mission name is required.");
        }
        if (type == null || duration <= 0) {
            throw new IllegalArgumentException("Mission type and duration are required.");
        }
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.requiredSkill = requiredSkill;
        this.requiredEquipment = requiredEquipment;
        this.targetFacility = targetFacility;
    }

    public String getName() {
        return name;
    }

    public MissionType getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public String getRequiredSkill() {
        return requiredSkill;
    }

    public String getRequiredEquipment() {
        return requiredEquipment;
    }

    public Facility getTargetFacility() {
        return targetFacility;
    }

    public MissionState getState() {
        return state;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Map<ResourceType, Integer> getResourceRequirements() {
        return resourceRequirements;
    }

    public void requireResource(ResourceType type, int amount) {
        if (type == null || amount < 0) {
            throw new IllegalArgumentException("Resource and non-negative amount are required.");
        }
        resourceRequirements.put(type, amount);
    }

    public void startProgress() {
        if (state == MissionState.ASSIGNED) {
            state = MissionState.IN_PROGRESS;
        }
    }

    public void advanceOneUnit() {
        if (state == MissionState.IN_PROGRESS) {
            elapsedTime++;
            if (elapsedTime >= duration) {
                state = MissionState.COMPLETED;
            }
        }
    }

    public void fail(String reason) {
        if (!isFinished()) {
            state = MissionState.FAILED;
            failureReason = reason;
        }
    }

    public boolean isFinished() {
        return state == MissionState.COMPLETED || state == MissionState.FAILED;
    }

    @Override
    public String toString() {
        String result = name + " [" + type + ", state=" + state
                + ", progress=" + elapsedTime + "/" + duration;
        if (!failureReason.isEmpty()) {
            result += ", reason=" + failureReason;
        }
        return result + "]";
    }
}
