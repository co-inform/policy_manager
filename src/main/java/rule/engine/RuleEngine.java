package rule.engine;

import model.ModelProperties;

import java.util.Set;

/**
 * A rule engine checks the properties of a post based on a number of rules.
 * Passing a callback objects allows to react to the evaluation results.
 *
 * Properties refer to values that were computed by the different modules.
 * Rules are loaded from rule files at the engine's startup.
 * Callback objects provide methods that are invoked by defined rules.
 *
 * @author Ipek Baris
 */
public interface RuleEngine {

    /**
     * Checks the properties against the set of loaded rules.
     * During the evaluation, specified actions can invoke methods of the passed callback object.
     *
     * @param properties properties to be checked
     * @param callback callback object with methods invoked by rule actions
     * @param modules set of the names of the modules whose rules to invoke
     */
    void check(ModelProperties properties, Callback callback, Set<String> modules);
}