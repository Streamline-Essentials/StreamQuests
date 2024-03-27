package host.plas.adapters.spigot;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpigotAdapter {
    private SpigotListener listener;

    private boolean enabled;

    public SpigotAdapter() {
        try {
            listener = new SpigotListener();

            enabled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
