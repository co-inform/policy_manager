package rule.engine;

import lombok.Getter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class RuleEngineConfig extends Properties {

    private final String THRESHOLD_PREFIX = "threshold_";

    @Getter
    public String[] moduleRulePaths;
    @Getter
    public String[] aggregationRulePaths;

    public RuleEngineConfig(){

    }

    public RuleEngineConfig(final URL configPath) {
        try {
            BufferedInputStream stream = new BufferedInputStream( configPath.openStream());
            this.load(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.initialize();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills all fields with the data defined in the config file.
     *
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void initialize() throws IllegalArgumentException,
            IllegalAccessException {
        Field[] fields = this.getClass().getFields();
        for (Field f : fields) {
            if (this.getProperty(f.getName()) == null) {
                System.err.print("Property '" + f.getName()
                        + "' not defined in config file");
            }
            if (f.getType().equals(String.class)) {
                f.set(this, this.getProperty(f.getName()));
            } else if (f.getType().equals(long.class)) {
                f.setLong(this, Long.valueOf(this.getProperty(f.getName())));
            } else if (f.getType().equals(int.class)) {
                f.setInt(this, Integer.valueOf(this.getProperty(f.getName())));
            } else if (f.getType().equals(boolean.class)) {
                f.setBoolean(this,
                        Boolean.valueOf(this.getProperty(f.getName())));
            } else if (f.getType().equals(String[].class)) {
                f.set(this, this.getProperty(f.getName()).split(";"));
            } else if (f.getType().equals(int[].class)) {
                String[] tmp = this.getProperty(f.getName()).split(";");
                int[] ints = new int[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    ints[i] = Integer.parseInt(tmp[i]);
                }
                f.set(this, ints);
            } else if (f.getType().equals(long[].class)) {
                String[] tmp = this.getProperty(f.getName()).split(";");
                long[] longs = new long[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    longs[i] = Long.parseLong(tmp[i]);
                }
                f.set(this, longs);
            }
        }
    }

    public Map<String, Object> getThresholds(){
        return entrySet().stream()
                .filter(s -> s.getKey().toString().startsWith(THRESHOLD_PREFIX))
                .collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> e.getValue()));
    }


}
