package rule.engine;

import lombok.Getter;
import lombok.Setter;
import model.Credibility;
import rule.engine.Callback;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PolicyEngineCallback implements Callback {

    @Getter
    private Map<String, Credibility> moduleCredibility;
    @Setter
    @Getter
    private Credibility finalCredibility;

    public PolicyEngineCallback() {
        moduleCredibility = new HashMap<>();
        this.finalCredibility = Credibility.not_verifiable;
    }

    public Credibility averageCredibility(Collection<Credibility> credibilityLabels) {
        return credibilityLabels.size() > 0 ?
                Credibility.values()
                        [credibilityLabels.stream()
                        .filter((credibility -> credibility != Credibility.not_verifiable))
                        .mapToInt(Enum::ordinal)
                        .sum() / (int) credibilityLabels.stream().filter((credibility -> credibility != Credibility.not_verifiable)).count()]
                : Credibility.not_verifiable;
    }

    public void social_translucence() {
        //todo: implement functionality
    }
}
