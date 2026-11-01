package marsops.facilities;

import marsops.common.FacilityType;

public class CommunicationStation extends Facility {
    public CommunicationStation(String name, String location, int capacity) {
        super(name, FacilityType.COMMUNICATION_STATION, location, capacity);
    }
}
