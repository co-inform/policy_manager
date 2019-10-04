import com.google.common.io.Resources;
import demo.Plugin;
import demo.SystemResponse;
import model.CoInformUserProperties;
import model.PostProperties;
import rule.engine.Callback;
import rule.engine.JEasyRuleEngine;
import rule.engine.RuleEngineConfig;
import rule.engine.RuleEngineFactory;
import utils.Vocabulary;

import java.util.HashMap;

public class UserPolicyTest {


    public static HashMap dummyRequest(){
        HashMap<String, Object> dummyUser = new HashMap<String, Object>();
        dummyUser.put("num_flag",6);
        dummyUser.put("num_evidence",0);
        dummyUser.put("userId",1234);
        dummyUser.put(Vocabulary.USER_GROUP, Vocabulary.USER_GROUP_NORMAL);

        return dummyUser;
    }

    public static CoInformUserProperties loadFacts(HashMap user){
        CoInformUserProperties facts = new CoInformUserProperties();
        facts.asMap().putAll(user);
        return facts;
    }

    public static void main(String[] args) {
        RuleEngineConfig config = new RuleEngineConfig(Resources.getResource("config/config.properties"));
        Callback systemResponse = new SystemResponse();

        try {
            JEasyRuleEngine engine = (JEasyRuleEngine) RuleEngineFactory.newInstance(Vocabulary.JEASY, config);
            CoInformUserProperties facts=loadFacts(dummyRequest());
            engine.check(facts, systemResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
