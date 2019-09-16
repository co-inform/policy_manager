package rule.engine;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import org.jeasy.rules.support.YamlRuleDefinitionReader;

import java.io.File;
import java.io.FileReader;


public class JEasyRuleEngine extends RuleEngine {

    private static volatile JEasyRuleEngine instance = null;
    private DefaultRulesEngine engine = new DefaultRulesEngine();
    private Rules rules;

    private JEasyRuleEngine() throws Exception {
        MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        rules = ruleFactory.createRules(new FileReader(super.rulePath));
    }

    public static JEasyRuleEngine getInstance() {
        if (instance == null) {
            synchronized (RuleEngine.class) {
                if (instance == null) {
                    try {
                        instance = new JEasyRuleEngine();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        return instance;
    }

    public void launch(Facts facts) throws Exception {
        engine.fire(this.rules, facts);
    }


}
