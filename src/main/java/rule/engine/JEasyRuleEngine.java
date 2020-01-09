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
import org.mvel2.PropertyAccessException;
import org.mvel2.UnresolveablePropertyException;
import utils.Vocabulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * The JEasyRuleEngine uses the JEasy framework to evaluate rules.
 *
 * @author Ipek Baris
 */
@Slf4j
public class JEasyRuleEngine implements RuleEngine {

    private DefaultRulesEngine engine = new DefaultRulesEngine();

    private Rules aggregationRules;
    private Map<String, Rules> moduleRules;

    private Map<String, Object> thresholds;


    /**
     * Creates a new JEasy-based rule engine instance, using a given configuration.
     *
     * @param config rule engine configuration
     */
    @SuppressWarnings("UnstableApiUsage")
    JEasyRuleEngine(@NonNull RuleEngineConfig config) throws IllegalArgumentException{
        aggregationRules = new Rules();
        moduleRules = new HashMap<>();
        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        ParserContext parserContext = new ParserContext();
        parserContext.addImport(Credibility.class);
        parserContext.addImport(Callback.class);
        parserContext.addPackageImport("java.util.Map");
        Arrays.stream(config.getAggregationRulePaths())
                .map(Resources::getResource)
                .forEach(url -> readRulesFromFile(ruleFactory, parserContext, url, aggregationRules));
        if (log.isDebugEnabled()) {
            log.debug("rulePaths:");
            config.getModuleRulePaths().forEach((key, value) -> log.debug("\t{}: {}", key, value));
        }
        config.getModuleRulePaths().entrySet()
                .forEach(entry -> {
                        Rules rules = new Rules();
                        moduleRules.put(entry.getKey(), rules);
                        readRulesFromFile(ruleFactory, parserContext, Resources.getResource(entry.getValue()), rules);
                });
        thresholds = config.getThresholds();
    }

    private void readRulesFromFile(MVELRuleFactory ruleFactory, ParserContext parserContext,  URL filePath, Rules rules) throws IllegalArgumentException {
        try {
            ruleFactory
                .createRules(new BufferedReader(new InputStreamReader(filePath.openStream())), parserContext)
                .iterator()
                .forEachRemaining(rules::register);
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
    public void check(ModelProperties properties, Callback callback, Set<String> modules) {
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

        // Tries to run the rules for the different modules. If the module response is not available it will fail.
        for (Map.Entry<String, Rules> nameRulePair: moduleRules.entrySet()) {
            if (modules.contains(nameRulePair.getKey())) {
                engine.fire(nameRulePair.getValue(), jProperties);
            }
        }

        log.debug("module labels:");
        for (Map.Entry<String, Credibility> entry : ((PolicyEngineCallback) jProperties.get(Vocabulary.CALLBACK)).getModuleCredibility().entrySet()) {
            log.debug("\t {}: {}", entry.getKey(), entry.getValue());
        }

        // Runs the aggregation rules
        engine.fire(this.aggregationRules, jProperties);
    }
}
