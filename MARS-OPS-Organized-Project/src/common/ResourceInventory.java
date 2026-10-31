import java.util.HashMap;
import java.util.Map;

public class ResourceInventory {
    private final Map<String, Integer> quantities = new HashMap<>();

    public ResourceInventory() {
        for (String type : ResourceNames.ALL) quantities.put(type, 0);
    }

    public int get(String type) { return quantities.get(type); }

    public void add(String type, int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        quantities.put(type, get(type) + amount);
    }

    public boolean remove(String type, int amount) {
        if (amount < 0 || get(type) < amount) return false;
        quantities.put(type, get(type) - amount);
        return true;
    }

    public String summary() {
        StringBuilder result = new StringBuilder();
        for (String type : ResourceNames.ALL) {
            result.append(type).append("=").append(get(type)).append(" ");
        }
        return result.toString().trim();
    }
}
