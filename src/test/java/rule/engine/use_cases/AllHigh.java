package rule.engine.use_cases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import model.Credibility;
import model.ModelProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import rule.engine.PolicyEngineCallback;
import rule.engine.RuleEngine;
import rule.engine.RuleEngineFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class AllHigh {

    RuleEngine ruleEngine;
    Set<String> ruleSet;
    Map<String, Object> moduleResponses;
    PolicyEngineCallback callback;
    Credibility expectedLabel;

    @Before
    public void setUp() throws IOException {
        ruleEngine = RuleEngineFactory.newInstance("jeasy");
        ruleSet = new HashSet<>();
        ruleSet.add("misinfome");
        ruleSet.add("claimcredibility");
        ruleSet.add("contentanalysis");
        moduleResponses = new LinkedHashMap<>();
        callback =  new PolicyEngineCallback();
    }

    @Test
    public void case1() {
        //edit these values
        moduleResponses.put("misinfome_credibility_value", 1);
        moduleResponses.put("misinfome_credibility_confidence", 1);
        moduleResponses.put("contentanalysis_veracity_true", 0.4);
        moduleResponses.put("contentanalysis_veracity_unknown", 0.3);
        moduleResponses.put("claimcredibility_tweet_claim_credibility_0_credibility", 1);
        moduleResponses.put("claimcredibility_tweet_claim_credibility_0_confidence", 1);
        expectedLabel = Credibility.credible_post;

        ruleEngine.check(new ModelProperties(moduleResponses), callback, ruleSet);

        //some log printouts
        log.info("module labels:");
        callback.getModuleCredibility().forEach((name, label) -> log.info("\t{}: {}", name, label));
        log.info("expected label: {}, actual label: {}", expectedLabel, callback.getFinalCredibility());

        assertThat(callback.getFinalCredibility()).isEqualTo(expectedLabel);
    }

    @Test
    public void case2() {
        //edit these values
        moduleResponses.put("misinfome_credibility_value", 0.7);
        moduleResponses.put("misinfome_credibility_confidence", 0.8);
        moduleResponses.put("contentanalysis_veracity_true", 0.32);
        moduleResponses.put("contentanalysis_veracity_unknown", 0.3);
        moduleResponses.put("claimcredibility_tweet_claim_credibility_0_credibility", 0.8);
        moduleResponses.put("claimcredibility_tweet_claim_credibility_0_confidence", 0.9);
        expectedLabel = Credibility.credible_post;

        ruleEngine.check(new ModelProperties(moduleResponses), callback, ruleSet);

        //some log printouts
        log.info("module labels:");
        callback.getModuleCredibility().forEach((name, label) -> log.info("\t{}: {}", name, label));
        log.info("expected label: {}, actual label: {}", expectedLabel, callback.getFinalCredibility());

        assertThat(callback.getFinalCredibility()).isEqualTo(expectedLabel);
    }

    @Test
    public void case3() {
        //edit these values
        moduleResponses.put("misinfome_credibility_value", 0.58);
        moduleResponses.put("misinfome_credibility_confidence", 0.65);
        moduleResponses.put("contentanalysis_veracity_true", 0.25);
        moduleResponses.put("contentanalysis_veracity_unknown", 0.4);
        moduleResponses.put("claimcredibility_tweet_claim_credibility_0_credibility", 0.4);
        moduleResponses.put("claimcredibility_tweet_claim_credibility_0_confidence", 0.9);
        expectedLabel = Credibility.mostly_credible_post;

        ruleEngine.check(new ModelProperties(moduleResponses), callback, ruleSet);

        //some log printouts
        log.info("module labels:");
        callback.getModuleCredibility().forEach((name, label) -> log.info("\t{}: {}", name, label));
        log.info("expected label: {}, actual label: {}", expectedLabel, callback.getFinalCredibility());

        assertThat(callback.getFinalCredibility()).isEqualTo(expectedLabel);
    }
}
