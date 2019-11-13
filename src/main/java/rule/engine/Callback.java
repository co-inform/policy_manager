package rule.engine;

import model.Credibility;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Callback is a generic interface to react on the fact checking result from outside the engine.
 * When the rules are evaluated on the facts, the defined actions are executed.
 * These invoke the specified methods of the callback object which has been passed into the engine.
 *
 * @author Ipek Baris
 */
public interface Callback {

    /*
    public Map<String, Credibility> getModuleCredibility();
    public Credibility getFinalCredibility();
    public Credibility setFinalCredibility(Credibility credibility);

     */
}
