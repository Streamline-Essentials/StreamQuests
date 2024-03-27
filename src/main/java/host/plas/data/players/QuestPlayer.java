package host.plas.data.players;

import host.plas.StreamQuests;
import host.plas.data.Quest;
import host.plas.data.require.RequirementType;
import lombok.Getter;
import lombok.Setter;
import net.streamline.api.data.console.StreamSender;
import net.streamline.api.loading.Loadable;
import net.streamline.api.utils.UserUtils;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;

@Getter @Setter
public class QuestPlayer implements Loadable<QuestPlayer> {
    private String identifier;
    private ConcurrentSkipListMap<String, Date> completedQuests;
    private ConcurrentSkipListMap<RequirementType, ConcurrentSkipListMap<String, Double>> questValues;
    private double points;

    public QuestPlayer(String identifier) {
        this.identifier = identifier;

        this.completedQuests = new ConcurrentSkipListMap<>();
        this.questValues = new ConcurrentSkipListMap<>();
        this.points = 0;
    }

    @Override
    public void save() {
        StreamQuests.getKeeper().save(this);
    }

    @Override
    public QuestPlayer augment(CompletableFuture<Optional<QuestPlayer>> augmentation) {
        CompletableFuture.runAsync(() -> {
            Optional<QuestPlayer> optional = augmentation.join();
            if (optional.isEmpty()) return;
            QuestPlayer player = optional.get();

            this.completedQuests.putAll(player.completedQuests);
            this.questValues.putAll(player.questValues);
            this.points += player.points;
        });

        return this;
    }

    public QuestPlayer addPoints(double points) {
        this.points += points;
        return this;
    }

    public QuestPlayer removePoints(double points) {
        this.points -= points;
        return this;
    }

    public QuestPlayer completeQuest(String quest) {
        this.completedQuests.put(quest, new Date());
        return this;
    }

    public QuestPlayer uncompleteQuest(String quest) {
        this.completedQuests.remove(quest);
        return this;
    }

    public QuestPlayer setValue(RequirementType type, String value, Double amount) {
        if (! this.questValues.containsKey(type)) {
            this.questValues.put(type, new ConcurrentSkipListMap<>());
        }

        this.questValues.get(type).put(value, amount);

        return this;
    }

    public Optional<Double> getValue(RequirementType type, String value) {
        if (! this.questValues.containsKey(type)) {
            return Optional.empty();
        }

        Double d = this.questValues.get(type).get(value);
        return d == null ? Optional.empty() : Optional.of(d);
    }

    public QuestPlayer resetValue(RequirementType type, String value) {
        if (! this.questValues.containsKey(type)) {
            return this;
        }

        this.questValues.get(type).remove(value);

        return this;
    }

    public QuestPlayer removeValue(RequirementType type, String value, double amount) {
        return setValue(type, value, getValue(type, value).orElse(0d) - amount);
    }

    public QuestPlayer addValue(RequirementType type, String value, double amount) {
        return setValue(type, value, getValue(type, value).orElse(0d) + amount);
    }

    public QuestPlayer completeQuest(Quest quest) {
        return completeQuest(quest.getIdentifier());
    }

    public QuestPlayer uncompleteQuest(Quest quest) {
        return uncompleteQuest(quest.getIdentifier());
    }

    public boolean hasCompletedQuest(Quest quest) {
        return hasCompletedQuest(quest.getIdentifier());
    }

    public boolean hasCompletedQuest(String identifier) {
        return this.completedQuests.keySet().stream().anyMatch(a -> a.equals(identifier)) && this.completedQuests.get(identifier) != null;
    }

    public StreamSender asSender() {
        return UserUtils.getOrCreateSender(getIdentifier());
    }

    public Optional<String> getNthLatestQuest(int n) {
        String r = getCompletedQuestsInOrderDESC().get(n);
        return r == null ? Optional.empty() : Optional.of(r);
    }

    public Optional<String> getNthCompletedQuest(int n) {
        String r = getCompletedQuestsInOrderASC().get(n);
        return r == null ? Optional.empty() : Optional.of(r);
    }

    public ConcurrentSkipListMap<Date, String> getCompletedByDate() {
        ConcurrentSkipListMap<Date, String> r = new ConcurrentSkipListMap<>();

        getCompletedQuests().forEach((string, date) -> {
            r.put(date, string);
        });

        return r;
    }

    public ConcurrentSkipListMap<Integer, String> getCompletedQuestsInOrderDESC() {
        ConcurrentSkipListMap<Integer, String> r = new ConcurrentSkipListMap<>();

        for (Map.Entry<Date, String> entry : getCompletedByDate().descendingMap().entrySet()) {
            String quest = entry.getValue();

            r.put(r.size(), quest);
        }

        return r;
    }

    public ConcurrentSkipListMap<Integer, String> getCompletedQuestsInOrderASC() {
        ConcurrentSkipListMap<Integer, String> r = new ConcurrentSkipListMap<>();

        for (Map.Entry<Date, String> entry : getCompletedByDate().entrySet()) {
            String quest = entry.getValue();

            r.put(r.size(), quest);
        }

        return r;
    }
}
