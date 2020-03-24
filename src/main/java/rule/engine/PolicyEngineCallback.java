package rule.engine;

import lombok.Getter;
import lombok.Setter;
import model.Credibility;
import rule.engine.Callback;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PolicyEngineCallback implements Callback {

    @Getter
    private Map<String, Credibility> moduleCredibility;
    @Setter
    @Getter
    private Credibility finalCredibility;

    private Map<String, Module> moduleMap;

    public PolicyEngineCallback() {
        moduleCredibility = new HashMap<>();
        moduleMap = new HashMap<>();
        this.finalCredibility = Credibility.not_verifiable;
    }

    public void setConfidence(String module, Double conf) {
        Module mod = moduleMap.get(module);
        if (mod == null) {
            mod = new Module();
            moduleMap.put(module, mod);
        }
        mod.conf = conf;
    }

    public Double getConfidence(String module) throws IllegalArgumentException {
        if (!moduleMap.containsKey(module)) {
            throw new IllegalArgumentException("No such module");
        }
        return moduleMap.get(module).conf;
    }

    public void setCredibility(String module, Double cred) {
        Module mod = moduleMap.get(module);
        if (mod == null) {
            mod = new Module();
            moduleMap.put(module, mod);
        }
        mod.cred = cred;
    }

    public Double getCredibility(String module) throws IllegalArgumentException {
        if (!moduleMap.containsKey(module)) {
            throw new IllegalArgumentException("No such module");
        }
        return moduleMap.get(module).cred;
    }

    public void setWeight(String module, Double weight) {
        Module mod = moduleMap.get(module);
        if (mod == null) {
            mod = new Module();
            moduleMap.put(module, mod);
        }
        mod.weight = weight;
    }

    public Double getWeight(String module) throws IllegalArgumentException {
        if (!moduleMap.containsKey(module)) {
            throw new IllegalArgumentException("No such module");
        }
        return moduleMap.get(module).weight;
    }

    public Collection<String> getModules() {
        return moduleMap.keySet();
    }

    public Map<String, Map<String, Double>> getValues() {
        Map<String, Map<String, Double>> valueMap = new HashMap<String, Map<String, Double>>();
        moduleMap.forEach((module, values) -> {
            Map<String, Double> map = new HashMap<String, Double>();
            map.put("credibility", values.cred);
            map.put("confidence", values.conf);
            valueMap.put(module, map);
        });
        return valueMap;
    }

    public Collection<Double> getConfidenceList() {
        return moduleMap.values().stream().map(module -> module.conf).collect(Collectors.toList());
    }

    public Collection<Double> getCredibilityList() {
        return moduleMap.values().stream().map(module -> module.cred).collect(Collectors.toList());
    }

    public Double max(Collection<Double> values) {
        return values.stream().reduce(Double.MIN_VALUE, Double::max);
    }

    public Double average(Collection<Double> values) {
        return values.size() == 0 ? 0 : values.stream().reduce(0D, Double::sum)/values.size();
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

    private static class Module {
        Double conf;
        Double cred;
        Double weight;
    }
}
