package demo;

import rule.engine.Callback;

/**
 * This class is created for demo.
 */
public class Plugin implements Callback {

    public void blur(){
        System.out.println("Post is blurred.");
    }

    public void showAnalysis(String key, float score) {
        StringBuilder builder = new StringBuilder();
        builder.append(key).append(" is ").append(score);
        System.out.println(builder.toString());
    }

}
