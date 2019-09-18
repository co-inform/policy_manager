package rule.engine;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;


public class RuleEngineConfig extends Properties {

    public String rulePath;
    public String demoRulePath;

    public RuleEngineConfig(final String configPath) {
        try {
            BufferedInputStream stream = new BufferedInputStream(
                    new FileInputStream(configPath));
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

    /**
     * @return path to the rule file
     */
    public String getRulePath() {
        return this.rulePath;
    }

    /**
     * @return path to the rule file for demo
     */
    public String getDemoRulePath() {
        return this.demoRulePath;
    }


}
