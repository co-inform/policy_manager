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
        int count = 0;
        int sum = 0;
        for (Credibility cred : credibilityLabels) {

            if (cred != Credibility.not_verifiable) {
                count++;
                sum += cred.ordinal();
            }
        }
        return Credibility.values()[sum / Math.max(1, count)]; //if count is 0 sum will also be 0
    }

    public void social_translucence() {
        //todo: implement functionality
    }
}
