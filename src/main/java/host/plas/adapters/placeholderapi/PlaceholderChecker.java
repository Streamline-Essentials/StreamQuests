package host.plas.adapters.placeholderapi;

import host.plas.StreamQuests;
import host.plas.data.QuestManager;
import host.plas.data.players.QuestPlayer;
import net.streamline.api.scheduler.BaseRunnable;
import net.streamline.api.utils.UserUtils;

public class PlaceholderChecker extends BaseRunnable {
    public PlaceholderChecker() {
        super(0, 20);
    }

    @Override
    public void run() {
        UserUtils.getOnlinePlayers().forEach((string, player) -> {
            QuestPlayer questPlayer = StreamQuests.getLoader().getOrCreate(player.getUuid());
            QuestManager.checkQuestRequirements(questPlayer);
        });
    }
}
