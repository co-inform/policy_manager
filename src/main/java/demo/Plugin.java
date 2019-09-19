package demo;

import rule.engine.Callback;

/**
 * This class is created for demo. It show the possible methods for plugin.
 */
public class Plugin implements Callback {

    public void blur(long postId, float thresholdBlurPercentage){
        StringBuilder builder = new StringBuilder();
        builder.append(postId).append(" is blurred with percentage of ").append(thresholdBlurPercentage);
        System.out.println(builder.toString());
    }

    public void blurProps(long postId,String... props){
        StringBuilder builder = new StringBuilder();
        builder.append(postId);
        for (int i = 0; i < props.length; i++)
            builder = builder.append(" ").append(props[i]);
        builder.append(" blurred.");
        System.out.println(builder.toString());
    }


    public void showAnalysis(String key, float score) {
        StringBuilder builder = new StringBuilder();
        builder.append(key).append(" is ").append(score);
        System.out.println(builder.toString());
    }

    public void place(String key,String... props) {
        StringBuilder builder = new StringBuilder();
        builder.append(key).append(" place ");
        for (int i = 0; i < props.length; i++)
            builder = builder.append(" ").append(props[i]);
        System.out.println(builder.toString());
    }

}
