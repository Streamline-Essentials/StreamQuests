package host.plas;

import host.plas.adapters.placeholderapi.PlaceholderApiAdapter;
import host.plas.adapters.spigot.SpigotAdapter;
import host.plas.commands.GrantCommand;
import host.plas.commands.ReloadCommand;
import host.plas.commands.RevokeCommand;
import host.plas.configs.QuestConfig;
import host.plas.database.Keeper;
import host.plas.database.MyLoader;
import host.plas.events.QuestsListener;
import host.plas.ratapi.QuestExpansion;
import lombok.Getter;
import lombok.Setter;
import net.streamline.api.modules.ModuleUtils;
import net.streamline.api.modules.SimpleModule;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;

public class StreamQuests extends SimpleModule {
    @Getter @Setter
    private static StreamQuests instance; // This will be used to access the module instance from anywhere in the plugin.

    @Getter @Setter
    private static QuestConfig exampleConfig; // This will be used to access the config instance from anywhere in the plugin.
    @Getter @Setter
    private static QuestsListener questsListener; // This will be used to access the listener instance from anywhere in the plugin.
    @Getter @Setter
    private static QuestExpansion exampleExpansion; // This will be used to access the expansion instance from anywhere in the plugin.

    @Getter @Setter
    private static Keeper keeper; // This will be used to access the database instance from anywhere in the plugin.
    @Getter @Setter
    private static MyLoader loader; // This will be used to access the loader instance from anywhere in the plugin.

    @Getter @Setter
    private static SpigotAdapter spigotAdapter;
    @Getter @Setter
    private static PlaceholderApiAdapter placeholderApiAdapter;

    public StreamQuests(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void registerCommands() {
        setCommands(new ArrayList<>(List.of(
                new GrantCommand(),
                new ReloadCommand(),
                new RevokeCommand()
        )));
    }

    @Override
    public void onEnable() {
        instance = this; // Set the instance to this module upon enabling.

        exampleConfig = new QuestConfig(); // Initialize the config.

        keeper = new Keeper(); // Initialize the database.
        loader = new MyLoader(); // Initialize the loader.

        spigotAdapter = new SpigotAdapter();
        placeholderApiAdapter = new PlaceholderApiAdapter();

        questsListener = new QuestsListener(); // Initialize the listener.

        exampleExpansion = new QuestExpansion(); // Initialize the expansion.
    }
}
