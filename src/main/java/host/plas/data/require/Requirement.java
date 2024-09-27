package host.plas.data.require;

import host.plas.utils.ItemUtils;
import lombok.Getter;
import lombok.Setter;
import tv.quaint.thebase.lib.re2j.Matcher;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import tv.quaint.objects.Identifiable;
import tv.quaint.utils.MatcherUtils;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

@Getter @Setter
public class Requirement implements Identifiable {
    private String identifier;
    private RequirementType type;
    private String value;
    private double amount;

    public Requirement(String identifier, RequirementType type, String value, double amount) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
        this.amount = amount;
    }

    public ConcurrentSkipListMap<Integer, ItemStack> getItems() {
        ConcurrentSkipListMap<Integer, ItemStack> items = new ConcurrentSkipListMap<>();

        Matcher matcher = MatcherUtils.matcherBuilder("item:nbt=(.*?);!", value);
        List<String[]> groups = MatcherUtils.getGroups(matcher, 3);
        for (String[] group : groups) {
            try {
                ItemStack item = ItemUtils.deserialize(group[0]);
                items.put(items.size(), item);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return items;
    }

    public void loadItems(List<ItemStack> items) {
        StringBuilder builder = new StringBuilder();
        for (ItemStack item : items) {
            String serialized = ItemUtils.serialize(item);
            builder.append("item:nbt=").append(serialized).append(";!");
        }

        value = builder.toString();
    }
}
