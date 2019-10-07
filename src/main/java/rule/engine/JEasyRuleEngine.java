package rule.engine;

import com.google.common.io.Resources;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import model.ModelProperties;
import model.PostProperties;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import utils.Vocabulary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

/**
 * The JEasyRuleEngine uses the JEasy framework to evaluate rules.
 *
 * @author Ipek Baris
 */
@Slf4j
public class JEasyRuleEngine implements RuleEngine {

    private DefaultRulesEngine engine = new DefaultRulesEngine();

    private Rules rules;

    private Map<String, Object> thresholds;


    /**
     * Creates a new JEasy-based rule engine instance, using a given configuration.
     *
     * @param config rule engine configuration
     */
    JEasyRuleEngine(@NonNull RuleEngineConfig config) {
        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        try {
            URL ruleURL = Resources.getResource(config.getRulePath());
            ruleFactory.createRules(new BufferedReader(new InputStreamReader(ruleURL.openStream())));
            thresholds = config.getThresholds();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load rule file!", e);
        } catch (Exception e) {
            // TODO this is a bad API of MVELRuleFactory, when does it throw exceptions anyway?
            throw new IllegalStateException("Failed to create rule factory!", e);
        }
    }

    @Override
    public void check(ModelProperties properties, Callback callback) {
        // copy properties to JEasy facts object
        Facts jProperties = new Facts();
        for (Map.Entry<String, Object> entry : properties.asMap().entrySet()) {
            jProperties.put(entry.getKey(), entry.getValue());
        }
        // populate the callback object to the rules
        jProperties.put(Vocabulary.CALLBACK, callback);
        // add thresholds of modules
        for (Map.Entry<String, Object> entry : thresholds.entrySet()) {
            jProperties.put(entry.getKey(), entry.getValue());
        }
        // use the JEasy engine to evaluate rules on given properties
        engine.fire(this.rules, jProperties);
    }
}
