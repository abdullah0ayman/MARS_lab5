package marsops.facilities;

import marsops.common.FacilityType;

public class PowerStation extends Facility {
    public PowerStation(String name, String location, int capacity) {
        super(name, FacilityType.POWER_STATION, location, capacity);
    }
}
