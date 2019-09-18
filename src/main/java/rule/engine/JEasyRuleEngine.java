package rule.engine;

import model.PostProperties;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * The JEasyRuleEngine uses the JEasy framework to evaluate rules.
 *
 * @author Ipek Baris
 */
public class JEasyRuleEngine implements RuleEngine {

    private DefaultRulesEngine engine = new DefaultRulesEngine();

    private Rules rules;

    /**
     * Creates a new JEasy-based rule engine instance, using a given configuration.
     *
     * @param config rule engine configuration
     */
    JEasyRuleEngine(RuleEngineConfig config) {
        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        try {
            rules = ruleFactory.createRules(new FileReader(config.getRulePath()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load rule file!", e);
        } catch (Exception e) {
            // TODO this is a bad API of MVELRuleFactory, when does it throw exceptions anyway?
            throw new IllegalStateException("Failed to create rule factory!", e);
        }
    }

    @Override
    public void check(PostProperties properties, Callback callback) {
        // copy properties to JEasy facts object
        Facts jProperties = new Facts();
        for (Map.Entry<String, Object> entry : properties.asMap().entrySet()) {
            jProperties.put(entry.getKey(), entry.getValue());
        }
        // populate the callback object to the rules
        jProperties.put("callback", callback);
        // use the JEasy engine to evaluate rules on given properties
        engine.fire(this.rules, jProperties);
    }
}
