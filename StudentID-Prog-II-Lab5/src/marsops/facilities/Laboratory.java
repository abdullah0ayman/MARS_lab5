package marsops.facilities;

import marsops.common.FacilityType;

public class Laboratory extends Facility {
    public Laboratory(String name, String location, int capacity) {
        super(name, FacilityType.LABORATORY, location, capacity);
    }
}
