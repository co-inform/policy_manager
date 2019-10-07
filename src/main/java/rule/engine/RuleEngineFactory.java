package rule.engine;

import com.google.common.io.Resources;
import utils.Vocabulary;

public class RuleEngineFactory {

    public static RuleEngine newInstance(String engine, RuleEngineConfig config) {
        if (Vocabulary.JEASY.equals(engine)) {
            return new JEasyRuleEngine(config);
        } else {
            throw new IllegalArgumentException("Unknown rule engine \"" + engine + "\"!");
        }
    }

    public static RuleEngine newInstance(String engine) {
        RuleEngineConfig config = new RuleEngineConfig(Resources.getResource("config/config.properties"));
        if (Vocabulary.JEASY.equals(engine)) {
            return new JEasyRuleEngine(config);
        } else {
            throw new IllegalArgumentException("Unknown rule engine \"" + engine + "\"!");
        }
    }
}


