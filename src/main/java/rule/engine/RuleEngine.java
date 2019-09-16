package rule.engine;

import utils.Config;

public abstract class RuleEngine {

    private Config config = new Config("src/main/resources/config/config.properties");
    protected String rulePath = config.getRulePath();

    void initialize() throws Exception {
    }

}


