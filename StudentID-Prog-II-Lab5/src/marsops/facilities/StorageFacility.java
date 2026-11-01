package marsops.facilities;

import marsops.common.FacilityType;

public class StorageFacility extends Facility {
    public StorageFacility(String name, String location, int capacity) {
        super(name, FacilityType.STORAGE, location, capacity);
    }
}
