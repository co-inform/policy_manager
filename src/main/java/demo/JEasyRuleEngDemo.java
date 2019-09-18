package demo;

import model.PostProperties;
import rule.engine.Callback;
import rule.engine.JEasyRuleEngine;
import rule.engine.RuleEngineFactory;
import rule.engine.RuleEngineConfig;

import java.util.HashMap;

/**
 * This demo presents how rule engine will be used. Facts will come from other modules, and engine takes the facts as input,
 * along with pre-defined rules it triggers the engine.
 * triggers the callback functions
 *     engine = RulesEngine.getInstance();
 *     callback = new Callback();
 *     facts = loadFacts(request.getContent());
 *     engine.check(facts, callback);
 *     // -> trigger callback.postIsHot()
 *     @author Ipek Baris
 */
public class JEasyRuleEngDemo {

    public static HashMap dummyRequest(){
        HashMap<String, Object> dummyContent = new HashMap<String, Object>();
        dummyContent.put("similarity",0.5f);
        return dummyContent;
    }

    public static PostProperties loadFacts(HashMap content){
        PostProperties facts = new PostProperties();
        facts.asMap().putAll(content);
        return facts;
    }

    // TODO replace log configuration instead of sysout
    public static void main(String[] args) {
        RuleEngineConfig config = new RuleEngineConfig("src/test/resources/config/config.properties");
        Callback plugin = new Plugin();

        try {
        JEasyRuleEngine engine = (JEasyRuleEngine) RuleEngineFactory.newInstance("jeasy", config);
        PostProperties facts=loadFacts(dummyRequest());
        engine.check(facts, plugin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
