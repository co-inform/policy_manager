package rule.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class RuleEngineConfigTest {

    RuleEngineConfig ruleEngineConfig = new RuleEngineConfig();
    Properties props = new Properties();

    @Before
    public void initTest(){

        props.put("threshold_similarity", 0.5f);
        props.put("threshold_stance", 0.5f);
        props.put("dummyProperty", "dummy value");

        ruleEngineConfig.putAll(props);


    }


    /**
     * Test case for reading only thresholds of the modules given in config.properties
     */
    @Test
    public void getThresholds() {
        Map<String, Object> thresholds = ruleEngineConfig.getThresholds();
        Assert.assertEquals(2,thresholds.keySet().stream().count());
        Assert.assertTrue(thresholds.containsKey("threshold_similarity"));
    }
}