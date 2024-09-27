package host.plas.adapters.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import singularity.holders.CosmicDependencyHolder;

import java.util.UUID;

public class PlaceholderApiHolder extends CosmicDependencyHolder<PlaceholderAPIPlugin> {
    public PlaceholderApiHolder() {
        super("placeholderapi", "PlaceholderAPI");
    }

    public String replacePlaceholders(String uuid, String string) {
        try {
            UUID u = UUID.fromString(uuid);
            OfflinePlayer player = Bukkit.getOfflinePlayer(u);
            return PlaceholderAPI.setPlaceholders(player, string);
        } catch (Exception e) {
            e.printStackTrace();
            return string;
        }
    }
}
