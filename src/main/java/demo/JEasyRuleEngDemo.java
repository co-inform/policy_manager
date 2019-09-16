package demo;

import org.jeasy.rules.api.Facts;
import rule.engine.JEasyRuleEngine;
import rule.engine.RuleEngineFactory;


public class JEasyRuleEngDemo {

    // TODO replace log configuration instead of sysout
    public static void main(String[] args) {
        JEasyRuleEngine engine = (JEasyRuleEngine) RuleEngineFactory.getInstance("jeasy");
        // score thresholds
        float similarity_threshold = 0.5f;
        float semantic_threshold = 0.5f;
        float stance_threshold = 0.5f;
        float veracity_threshold = 0.5f;
        float credibility_threshold = 0.5f;

        Facts facts = new Facts();
        Post.PostBuilder builder = new Post.PostBuilder();
        Post post = builder.setSimilarity(0.5f).getPost();
        facts.put("post",  post);
        facts.put("plugin",  new Plugin());


        try {
            engine.launch(facts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
