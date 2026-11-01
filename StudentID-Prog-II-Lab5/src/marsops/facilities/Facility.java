package marsops.facilities;

import java.util.ArrayList;
import java.util.List;
import marsops.common.FacilityType;
import marsops.common.ResourceInventory;

public class Facility {
    private final String name;
    private final FacilityType type;
    private final String location;
    private final int capacity;
    private boolean operational = true;
    private final ResourceInventory resources = new ResourceInventory();
    private final List<String> equipment = new ArrayList<>();

    public Facility(String name, FacilityType type, String location, int capacity) {
        if (name == null || name.isBlank() || type == null || capacity < 0) {
            throw new IllegalArgumentException("Valid facility information is required.");
        }
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public FacilityType getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    public ResourceInventory getResources() {
        return resources;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void addEquipment(String item) {
        if (item != null && !item.isBlank()) {
            equipment.add(item);
        }
    }

    public boolean hasEquipment(String item) {
        return item == null || equipment.contains(item);
    }

    @Override
    public String toString() {
        return name + " (" + type + ", operational=" + operational
                + ", " + resources.summary() + ")";
    }
}
