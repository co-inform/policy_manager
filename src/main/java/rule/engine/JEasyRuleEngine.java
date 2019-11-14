package rule.engine;

import com.google.common.io.Resources;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import model.Credibility;
import model.ModelProperties;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import org.mvel2.ParserContext;
import utils.Vocabulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
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
    @SuppressWarnings("UnstableApiUsage")
    JEasyRuleEngine(@NonNull RuleEngineConfig config) throws IllegalArgumentException{
        rules = new Rules();
        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        ParserContext parserContext = new ParserContext();
        parserContext.addImport(Credibility.class);
        parserContext.addImport(Callback.class);
        parserContext.addPackageImport("java.util.Map");
        Arrays.stream(config.getRulePaths())
                .map(Resources::getResource)
                .forEach(url -> readRulesFromFile(ruleFactory, parserContext, url));
        thresholds = config.getThresholds();
    }

    private void readRulesFromFile(MVELRuleFactory ruleFactory, ParserContext parserContext,  URL filePath) throws IllegalArgumentException {
        try {
            ruleFactory
                .createRules(new BufferedReader(new InputStreamReader(filePath.openStream())), parserContext)
                .iterator()
                .forEachRemaining(rule -> this.rules.register(rule));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load rule file!, "+filePath.toString()+ ", "+e.getMessage(), e);
        } catch (Exception e) {
            // TODO this is a bad API of MVELRuleFactory, when does it throw exceptions anyway?
            throw new IllegalStateException("Failed to create rule factory!", e);
        }
}

    /**
     * {@inheritDoc}
     */
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
