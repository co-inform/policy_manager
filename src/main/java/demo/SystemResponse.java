package demo;

import rule.engine.Callback;

public class SystemResponse implements Callback {

    public void restrict(String userId, String... actions){
        StringBuilder builder = new StringBuilder();
        builder.append("User Id").append(userId).append(":\nPermissions for the following actions have been changed.");
        for (int i = 0; i < actions.length; i++)
            builder = builder.append("\n").append(actions[i]);
        System.out.println(builder.toString());
    }

}
