package host.plas.adapters.placeholderapi;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlaceholderApiAdapter {
    private PlaceholderApiHolder holder;

    private boolean enabled;

    private PlaceholderChecker checker;

    public PlaceholderApiAdapter() {
        try {
            holder = new PlaceholderApiHolder();
            enabled = holder.isPresent();

            checker = new PlaceholderChecker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String replacePlaceholders(String uuid, String string) {
        if (! isEnabled()) return string;

        return getHolder().replacePlaceholders(uuid, string);
    }
}
