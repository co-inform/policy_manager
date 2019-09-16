package rule.engine;

public class RuleEngineFactory {

    public static RuleEngine getInstance(String engine) {
        if ("jeasy".equals(engine)) return JEasyRuleEngine.getInstance();
        else throw new IllegalArgumentException("unknown " + engine);
    }
}


