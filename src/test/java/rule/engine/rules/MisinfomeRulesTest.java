package rule.engine.rules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import model.Credibility;
import model.ModelProperties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import rule.engine.PolicyEngineCallback;
import rule.engine.RuleEngine;
import rule.engine.RuleEngineFactory;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class MisinfomeRulesTest {

    private String module;
    private RuleEngine ruleEngine;
    private Map<String, Object> flattenedModuleResponse;
    private Set<String> moduleSet;

    @SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes"})
    @Before
    public void setUp() throws IOException {
        // the relative path to a gateway response with the resource dir as base.
        String gatewayResponseFile = "gateway_response.json";
        // Set this to the name of the module. (The prefix of the key names in the flattened_module_responses) in the gateway response file.
        module = "misinfome";

        ruleEngine = RuleEngineFactory.newInstance("jeasy");
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> tree = om.readValue(Resources.getResource(gatewayResponseFile), LinkedHashMap.class);
        flattenedModuleResponse = (LinkedHashMap) tree.get("flattened_module_responses");

        moduleSet = new HashSet<>();
        moduleSet.add(module);
    }

    /**
     * Make sure the rule set is able to assign a credibility label for the module
     */
    @Test
    public void testAssignsModuleResponse() {
        PolicyEngineCallback callback = new PolicyEngineCallback();
        ruleEngine.check(new ModelProperties(flattenedModuleResponse), callback, moduleSet);
        assertThat(callback.getModuleCredibility().get(module)).isNotNull().isInstanceOf(Credibility.class);
    }

    /**
     * Make sure that the rule set is able to assign the not verifiable post label.
     */
    @Test
    public void testNotVerifiablePost() {
        Map<String, Object> mockChanges = new LinkedHashMap<>();

        // set the necessary attributes to be a not verifiable post
        mockChanges.put("misinfome_credibility_confidence", -1);

        PolicyEngineCallback callback = new PolicyEngineCallback();
        ModelProperties modelProperties = new ModelProperties(mockResponse(flattenedModuleResponse, mockChanges));
        ruleEngine.check(modelProperties, callback, moduleSet);
        assertThat(callback.getModuleCredibility().get(module)).isNotNull().isEqualTo(Credibility.not_verifiable);
    }

    /**
     * Make sure that the rule set is able to assign the not credible post label.
     */
    @Test
    public void testNotCrediblePost() {
        Map<String, Object> mockChanges = new LinkedHashMap<>();

        // set the necessary attributes to be a not credible post
        mockChanges.put("misinfome_credibility_confidence", 0.6);
        mockChanges.put("misinfome_credibility_value", -0.7);

        PolicyEngineCallback callback = new PolicyEngineCallback();
        ModelProperties modelProperties = new ModelProperties(mockResponse(flattenedModuleResponse, mockChanges));
        ruleEngine.check(modelProperties, callback, moduleSet);
        assertThat(callback.getModuleCredibility().get(module)).isNotNull().isEqualTo(Credibility.not_credible);
    }

    /**
     * Make sure that the rule set is able to assign the credible uncertain post label.
     */
    @Ignore
    @Test
    public void testCredibleUncertainPost() {
        Map<String, Object> mockChanges = new LinkedHashMap<>();

        // set the necessary attributes to be a credible uncertain post
        mockChanges.put("misinfome_credibility_confidence", 0.6);
        mockChanges.put("misinfome_credibility_value", 0);

        PolicyEngineCallback callback = new PolicyEngineCallback();
        ModelProperties modelProperties = new ModelProperties(mockResponse(flattenedModuleResponse, mockChanges));
        ruleEngine.check(modelProperties, callback, moduleSet);
        assertThat(callback.getModuleCredibility().get(module)).isNotNull().isEqualTo(Credibility.credibility_uncertain);
    }

    /**
     * Make sure that the rule set is able to assign the mostly credible post label.
     */
    @Test
    public void testMostlyCrediblePost() {
        Map<String, Object> mockChanges = new LinkedHashMap<>();

        // set the necessary attributes to be a mostly credible post
        mockChanges.put("misinfome_credibility_confidence", 0.6);
        mockChanges.put("misinfome_credibility_value", 0.4);

        PolicyEngineCallback callback = new PolicyEngineCallback();
        ModelProperties modelProperties = new ModelProperties(mockResponse(flattenedModuleResponse, mockChanges));
        ruleEngine.check(modelProperties, callback, moduleSet);
        assertThat(callback.getModuleCredibility().get(module)).isNotNull().isEqualTo(Credibility.mostly_credible);
    }

    /**
     * Make sure that the rule set is able to assign the credible post label.
     */
    @Test
    public void testCrediblePost() {
        Map<String, Object> mockChanges = new LinkedHashMap<>();

        // set the necessary attributes to be a mostly credible post
        mockChanges.put("misinfome_credibility_confidence", 0.6);
        mockChanges.put("misinfome_credibility_value", 0.7);

        PolicyEngineCallback callback = new PolicyEngineCallback();
        ModelProperties modelProperties = new ModelProperties(mockResponse(flattenedModuleResponse, mockChanges));
        ruleEngine.check(modelProperties, callback, moduleSet);
        assertThat(callback.getModuleCredibility().get(module)).isNotNull().isEqualTo(Credibility.credible);
    }

    private Map<String, Object> mockResponse(Map<String, Object> resp, Map<String, Object> mockChanges) {
        Map<String, Object> ret = new LinkedHashMap<>();
        resp.forEach(ret::put);
        mockChanges.forEach(ret::put);
        return ret;
    }
}
