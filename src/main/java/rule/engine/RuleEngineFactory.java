package rule.engine;

import utils.Vocabulary;

public class RuleEngineFactory {

    public static RuleEngine newInstance(String engine, RuleEngineConfig config) {
        if (Vocabulary.JEASY.equals(engine)) {
            return new JEasyRuleEngine(config);
        } else {
            throw new IllegalArgumentException("Unknown rule engine \"" + engine + "\"!");
        }
    }
}


