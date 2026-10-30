public class StorageFacility extends Facility {
    // Constructor...........
    public StorageFacility(String name, String location, int capacity) {
        super(name, location, capacity);
    }

    // Abstract method...............
    // Returnes "Storage Facility"...
    @Override
    public String getType() {
        return "Storage Facility";
    }
}