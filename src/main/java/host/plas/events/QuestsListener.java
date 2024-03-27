package host.plas.events;

import host.plas.StreamQuests;
import host.plas.data.players.QuestPlayer;
import net.streamline.api.data.players.StreamPlayer;
import net.streamline.api.events.server.LoginCompletedEvent;
import net.streamline.api.events.server.LogoutEvent;
import net.streamline.api.modules.ModuleUtils;
import tv.quaint.events.BaseEventListener;
import tv.quaint.events.processing.BaseProcessor;

public class QuestsListener implements BaseEventListener {
    public QuestsListener() {
        ModuleUtils.listen(this, StreamQuests.getInstance());
    }

    @BaseProcessor
    public void onPlayerJoin(LoginCompletedEvent event) {
        StreamPlayer player = event.getPlayer();

        StreamQuests.getLoader().getOrCreate(player.getUuid());
    }

    @BaseProcessor
    public void onPlayerLeave(LogoutEvent event) {
        StreamPlayer player = event.getPlayer();

        QuestPlayer p = StreamQuests.getLoader().getOrCreate(player.getUuid());

        p.save();

        StreamQuests.getLoader().unload(player.getUuid());
    }
}
