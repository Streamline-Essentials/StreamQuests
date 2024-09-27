package host.plas.adapters.placeholderapi;

import host.plas.StreamQuests;
import host.plas.data.QuestManager;
import host.plas.data.players.QuestPlayer;
import singularity.scheduler.BaseRunnable;
import singularity.utils.UserUtils;

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
