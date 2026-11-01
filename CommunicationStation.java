public class CommunicationStation extends Facility {
    // Constructor...........
    public CommunicationStation(String name, String location, int capacity) {
        super(name, location, capacity);
    }

    // Abstract method....................
    // Returnes "Communication Station"...
    @Override
    public String getType() {
        return "Communication Station";
    }
}