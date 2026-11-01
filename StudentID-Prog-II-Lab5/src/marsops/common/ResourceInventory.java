package marsops.common;

import java.util.EnumMap;
import java.util.Map;

public class ResourceInventory {
    private final Map<ResourceType, Integer> quantities =
            new EnumMap<>(ResourceType.class);

    public ResourceInventory() {
        for (ResourceType type : ResourceType.values()) {
            quantities.put(type, 0);
        }
    }

    public int get(ResourceType type) {
        return quantities.get(type);
    }

    public void add(ResourceType type, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        quantities.put(type, get(type) + amount);
    }

    public boolean remove(ResourceType type, int amount) {
        if (amount < 0 || get(type) < amount) {
            return false;
        }
        quantities.put(type, get(type) - amount);
        return true;
    }

    public String summary() {
        StringBuilder result = new StringBuilder();
        for (ResourceType type : ResourceType.values()) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(type).append("=").append(get(type));
        }
        return result.toString();
    }
}
