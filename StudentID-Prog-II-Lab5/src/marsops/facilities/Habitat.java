package marsops.facilities;

import marsops.common.FacilityType;
import marsops.common.LifeSupportSystem;

public class Habitat extends Facility {
    private final LifeSupportSystem lifeSupportSystem = new LifeSupportSystem();
    private boolean oxygenLeak;

    public Habitat(String name, String location, int capacity) {
        super(name, FacilityType.HABITAT, location, capacity);
    }

    public LifeSupportSystem getLifeSupportSystem() {
        return lifeSupportSystem;
    }

    public boolean hasOxygenLeak() {
        return oxygenLeak;
    }

    public void setOxygenLeak(boolean oxygenLeak) {
        this.oxygenLeak = oxygenLeak;
    }
}
