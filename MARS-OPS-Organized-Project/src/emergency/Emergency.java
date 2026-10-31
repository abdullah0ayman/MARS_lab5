public class Emergency {
    private final String type;
    private final String location;
    private String state = "DETECTED";
    private String outcome = "";
    public Emergency(String type, String location) { this.type = type; this.location = location; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public String getState() { return state; }
    public String getOutcome() { return outcome; }
    public void setState(String state) { this.state = state; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public String toString() { return type + " at " + location + " [" + state + (outcome.isEmpty() ? "" : ": " + outcome) + "]"; }
}
