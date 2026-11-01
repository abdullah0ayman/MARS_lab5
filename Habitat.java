// Habitat may has oxygenleak...
public class Habitat extends Facility {
    // True if there is oxygenleak...
    private boolean oxygenLeak;

    // Constructor.........................
    // Starts there is no oxygenleak...
    public Habitat(String name, String location, int capacity) {
        super(name, location, capacity);
        oxygenLeak = false;
    }

    // Abstract method.........
    // Returnes Habitat...
    @Override
    public String getType() {
        return "Habitat";
    }

    // Methods..................................
    // Oxygen leak has started in this habitat...
    public void startLeak() {
        oxygenLeak = true;
    }

    // The oxygen leak has been repaired...
    public void repairLeak() {
        oxygenLeak = false;
    }

    // True if the habitat currently has an oxygen leak...
    public boolean hasLeak() {
        return oxygenLeak;
    }

    // Removes oxygen from this habitat...
    // When a leak is detected...
    public void loseOxygen(int amount) {
        if (!oxygenLeak || amount <= 0)
            return;
        int oxygenAvailable = getResourceAmount("oxygen");
        if (amount > oxygenAvailable) {
            amount = oxygenAvailable;
        }
        removeResource("oxygen", amount);
    }
}