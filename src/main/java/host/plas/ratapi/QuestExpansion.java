package host.plas.ratapi;

import host.plas.StreamQuests;
import host.plas.data.Quest;
import host.plas.data.QuestManager;
import host.plas.data.players.QuestPlayer;
import net.streamline.api.data.console.StreamSender;
import net.streamline.api.placeholders.expansions.RATExpansion;
import net.streamline.api.placeholders.replaceables.IdentifiedReplaceable;
import net.streamline.api.placeholders.replaceables.IdentifiedUserReplaceable;
import tv.quaint.utils.MatcherUtils;

import java.util.Optional;

public class QuestExpansion extends RATExpansion {
    public QuestExpansion() {
        super(
                new RATExpansionBuilder(
                        "quests"
                )
        );
    }

    @Override
    public void init() {
        new IdentifiedUserReplaceable(
                this, // The expansion instance.
                "completed_current", // The argument (what comes after the "_" in the placeholder).
                (s, u) -> {
                    if (u.isConsole()) return s.string();
                    QuestPlayer player = StreamQuests.getLoader().getOrCreate(u.getUuid());
                    if (player == null) return s.string();
                    Optional<String> stringOptional = player.getNthLatestQuest(0);
                    return stringOptional.orElse("");
                }).register(); // Register the replaceable.

        new IdentifiedUserReplaceable(
                this, // The expansion instance.
                "completed_last", // The argument (what comes after the "_" in the placeholder).
                (s, u) -> {
                    if (u.isConsole()) return s.string();
                    QuestPlayer player = StreamQuests.getLoader().getOrCreate(u.getUuid());
                    if (player == null) return s.string();
                    Optional<String> stringOptional = player.getNthLatestQuest(1);
                    return stringOptional.orElse("");
                }).register(); // Register the replaceable.

        new IdentifiedUserReplaceable(
                this,
                MatcherUtils.makeLiteral("quest_") + "(.*?)",
                1,
                (s, u) -> {
                    String[] split = s.get().split("_", 2);
                    if (split.length < 2) {
                        return s.string();
                    }
                    String questName = split[0];
                    Optional<Quest> optional = QuestManager.getQuest(questName);
                    if (optional.isEmpty()) return s.string();

                    String computed = computeQuest(u, optional.get(), split[1]);
                    return computed == null ? s.string() : computed;
                }).register(); // Register the replaceable.
    }

    public String computeQuest(StreamSender sender, Quest quest, String params) {
        if (params.equals("pretty_name")) {
            return quest.getPrettyName();
        }

        return null;
    }
}
