package host.plas.data.require;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.objects.Identifiable;

@Getter @Setter
public class Requirement implements Identifiable {
    private String identifier;
    private RequirementType type;
    private String value;
    private double amount;

    public Requirement(String identifier, RequirementType type, String value, double amount) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
        this.amount = amount;
    }
}
