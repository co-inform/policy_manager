package model;

import utils.Vocabulary;


/**
 * A post has properties which are scores or metrics from different modules.
 * The post object will be handed into the @link{RuleEngine rule engine}.
 */
public class PostProperties extends ModelProperties {

        private int numCriticalScore = 0;
        //todo: how do we assign postId? not clear
        private String postId;

        public PostProperties() {
            // supporting conditions checking count
            super.properties.put(Vocabulary.CRITICAL_SCORE_COUNT, numCriticalScore);
        }

}
