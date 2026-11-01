public abstract class Facility {
    // The 7 resource names used in the colony...
    public static final String[] RESOURCE_NAMES = {
            "oxygen", "water", "food", "fuel", "battery", "medical supplies", "spare parts"
    };

    // #NUMs of MAX_EQUIPMENT...
    private static final int MAX_EQUIPMENT = 10;

    // common fields...
    // "operational == FALSE" means the facility is unavailable...
    // maybe fire...
    private String name;
    private String location;
    private int capacity;
    private boolean operational;

    // The amount of each resource...
    // Change only from method ( add / remove ) resource...
    private int oxygen;
    private int water;
    private int food;
    private int fuel;
    private int battery;
    private int medicalSupplies;
    private int spareParts;

    // 2 parallel arrays, number of active equip...
    private String[] equipmentNames;
    private boolean[] equipmentWorking;
    private int equipmentCount;

    // Constructor................
    // New facility is available "operational == TRUE"...
    public Facility(String name, String location, int capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        operational = true;
        equipmentNames = new String[MAX_EQUIPMENT];
        equipmentWorking = new boolean[MAX_EQUIPMENT];
        equipmentCount = 0;
    }

    // Getters.....................
    // Returns the facility name...
    public String getName() {
        return name;
    }

    // Returns the kocation of the facility...
    public String getLocation() {
        return location;
    }

    // Returns the capacity of the facility...
    public int getCapacity() {
        return capacity;
    }

    // Returns true if the facility is currently available...
    public boolean isOperational() {
        return operational;
    }

    // Setter.........................................................
    // Sets the facility as available (true) or unavailable (false)...
    public void setOperational(boolean value) {
        operational = value;
    }

    // Methods for Resourcess..................................
    // Calculate the amount of each resource in the facility...
    public int getResourceAmount(String resourceName) {
        if (resourceName == null) {
            return 0;
        }
        if (resourceName.equalsIgnoreCase("oxygen")) {
            return oxygen;
        }
        if (resourceName.equalsIgnoreCase("water")) {
            return water;
        }
        if (resourceName.equalsIgnoreCase("food")) {
            return food;
        }
        if (resourceName.equalsIgnoreCase("fuel")) {
            return fuel;
        }
        if (resourceName.equalsIgnoreCase("battery")) {
            return battery;
        }
        if (resourceName.equalsIgnoreCase("medical supplies")) {
            return medicalSupplies;
        }
        if (resourceName.equalsIgnoreCase("spare parts")) {
            return spareParts;
        }
        return 0;
    }

    // Add an amount of a resource to this facility...
    public void addResource(String resourceName, int amount) {
        if (resourceName == null || amount <= 0) {
            return;
        }
        if (resourceName.equalsIgnoreCase("oxygen")) {
            oxygen += amount;
        } else if (resourceName.equalsIgnoreCase("water")) {
            water += amount;
        } else if (resourceName.equalsIgnoreCase("food")) {
            food += amount;
        } else if (resourceName.equalsIgnoreCase("fuel")) {
            fuel += amount;
        } else if (resourceName.equalsIgnoreCase("battery")) {
            battery += amount;
        } else if (resourceName.equalsIgnoreCase("medical supplies")) {
            medicalSupplies += amount;
        } else if (resourceName.equalsIgnoreCase("spare parts")) {
            spareParts += amount;
        }
    }

    // Removes an amount of a resource from this facility...
    public boolean removeResource(String resourceName, int amount) {
        if (resourceName == null || amount <= 0 || getResourceAmount(resourceName) < amount) {
            return false;
        }
        if (resourceName.equalsIgnoreCase("oxygen")) {
            oxygen -= amount;
        } else if (resourceName.equalsIgnoreCase("water")) {
            water -= amount;
        } else if (resourceName.equalsIgnoreCase("food")) {
            food -= amount;
        } else if (resourceName.equalsIgnoreCase("fuel")) {
            fuel -= amount;
        } else if (resourceName.equalsIgnoreCase("battery")) {
            battery -= amount;
        } else if (resourceName.equalsIgnoreCase("medical supplies")) {
            medicalSupplies -= amount;
        } else if (resourceName.equalsIgnoreCase("spare parts")) {
            spareParts -= amount;
        } else {
            return false;
        }
        return true;
    }

    // Methods for equipmentss....
    // Add a new equipment item...
    public boolean addEquipment(String equipmentName) {
        if (equipmentName == null || equipmentCount >= equipmentNames.length) {
            return false;
        }
        equipmentNames[equipmentCount] = equipmentName;
        equipmentWorking[equipmentCount] = true;
        equipmentCount++;
        return true;
    }

    // Equipment is working or not if...
    public boolean setEquipmentWorking(String equipmentName, boolean working) {
        if (equipmentName == null) {
            return false;
        }
        for (int i = 0; i < equipmentCount; i++) {
            if (equipmentNames[i].equalsIgnoreCase(equipmentName)) {
                equipmentWorking[i] = working;
                return true;
            }
        }
        return false;
    }

    // Returns true if the facility has a working equipment item with this name...
    public boolean hasEquipment(String equipmentName) {
        if (equipmentName == null) {
            return false;
        }
        for (int i = 0; i < equipmentCount; i++) {
            if (equipmentWorking[i] && equipmentNames[i].equalsIgnoreCase(equipmentName)) {
                return true;
            }
        }
        return false;
    }

    // Abstrace method.............
    // Returns the facility type...
    public abstract String getType();
}