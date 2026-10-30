public class PowerStation extends Facility {
    // Constructor...........
    public PowerStation(String name, String location, int capacity) {
        super(name, location, capacity);
    }

    // Abstract method............
    // Returnes "Power Station"...
    @Override
    public String getType() {
        return "Power Station";
    }
}