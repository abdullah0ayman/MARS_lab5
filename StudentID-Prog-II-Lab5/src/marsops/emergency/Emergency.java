package marsops.emergency;

import marsops.common.EmergencyState;
import marsops.common.EmergencyType;

public class Emergency {
    private final EmergencyType type;
    private final String location;
    private EmergencyState state = EmergencyState.DETECTED;
    private String outcome = "";

    public Emergency(EmergencyType type, String location) {
        this.type = type;
        this.location = location;
    }

    public EmergencyType getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public EmergencyState getState() {
        return state;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setState(EmergencyState state) {
        this.state = state;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        String result = type + " at " + location + " [" + state;
        if (!outcome.isEmpty()) {
            result += ": " + outcome;
        }
        return result + "]";
    }
}
