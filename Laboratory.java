// Laboratory may catch fire...
public class Laboratory extends Facility {
    // True if there is fire...
    private boolean fireActive;

    // Constructor..............;
    // Starts there is no fire...
    public Laboratory(String name, String location, int capacity) {
        super(name, location, capacity);
        fireActive = false;
    }

    // Abstract method........
    // Returnes "Laboratory"...
    @Override
    public String getType() {
        return "Laboratory";
    }

    // Methods..................................
    // Starts a fire ---> becomes unavailable...
    public void startFire() {
        fireActive = true;
        setOperational(false);
    }

    // Stops fire...
    public void stopFire() {
        fireActive = false;
    }

    // True if there is a fire in the laboratory...
    public boolean hasFire() {
        return fireActive;
    }
}