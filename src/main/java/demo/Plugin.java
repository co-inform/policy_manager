package demo;

import rule.engine.Callback;

/**
 * This class is created for demo.
 */
public class Plugin implements Callback {

    public void blur(){
        System.out.println("PostProperties is blurred.");
    }

}
