package host.plas.commands;

import host.plas.StreamQuests;
import host.plas.data.Quest;
import host.plas.data.QuestManager;
import host.plas.data.players.QuestPlayer;
import net.streamline.api.command.ModuleCommand;
import net.streamline.api.command.StreamlineCommand;
import net.streamline.api.command.context.CommandContext;
import net.streamline.api.data.players.StreamPlayer;
import net.streamline.api.utils.UserUtils;

import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;

public class GrantCommand extends ModuleCommand {
    public GrantCommand() {
        super(StreamQuests.getInstance(), "grantquest", "streamline.command.grantquest.default", "gquest", "gqu");
    }

    @Override
    public void run(CommandContext<StreamlineCommand> context) {
        if (! context.isArgUsable(1)) {
            context.sendMessage("&cInvalid arguments. Must be /grantquest <player> <quest>");
            return;
        }

        Optional<StreamPlayer> optionalPlayer = context.getPlayerArg(0);
        if (optionalPlayer.isEmpty()) {
            context.sendMessage("&cInvalid player.");
            return;
        }
        StreamPlayer player = optionalPlayer.get();

        QuestPlayer questPlayer = StreamQuests.getLoader().getOrCreate(player.getUuid());
        if (questPlayer == null) {
            context.sendMessage("&cFailed to load player.");
            return;
        }

        String questName = context.getStringArg(1);

        Optional<Quest> optionalQuest = QuestManager.getQuest(questName);
        if (optionalQuest.isEmpty()) {
            context.sendMessage("&cInvalid quest.");
            return;
        }
        Quest quest = optionalQuest.get();

        if (questPlayer.hasCompletedQuest(quest)) {
            context.sendMessage("&cThis player has already completed this quest.");
            return;
        }

        quest.rewardPlayer(questPlayer);

        context.sendMessage("&aGranted &equest &8'&b" + questName + "&8' to player&8.");
    }

    @Override
    public ConcurrentSkipListSet<String> doTabComplete(CommandContext<StreamlineCommand> context) {
        if (context.getArgCount() == 1) {
            return UserUtils.getOnlinePlayers().values().stream().map(StreamPlayer::getCurrentName).collect(ConcurrentSkipListSet::new, ConcurrentSkipListSet::add, ConcurrentSkipListSet::addAll);
        }
        if (context.getArgCount() == 2) {
            return QuestManager.getQuests().stream().map(Quest::getIdentifier).collect(ConcurrentSkipListSet::new, ConcurrentSkipListSet::add, ConcurrentSkipListSet::addAll);
        }

        return new ConcurrentSkipListSet<>();
    }
}
